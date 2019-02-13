import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import me.lucko.luckperms.api.Group
import me.lucko.luckperms.api.LuckPermsApi
import me.lucko.luckperms.api.User
import net.darkdevelopers.darkbedrock.darkness.general.functions.toNonNull
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.util.*

/**
 * Created by DevSnox on 12.02.18
 * Copyright (c) 2018 DevSnox
 * GitHub: https://github.com/DevSnox
 * Web: http://devsnox.me
 * Mail: me.devsnox@gmail.com
 * Discord: DevSnox#4884 | Skype: live:chaos3729
 */
class TablistModule : Module, Listener(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)) {

    private val scoreboard: Scoreboard
        get() = Bukkit.getScoreboardManager().mainScoreboard

    private val luckPermsApi: LuckPermsApi
        get() = Bukkit.getServicesManager().getRegistration(LuckPermsApi::class.java)?.provider.toNonNull("LuckPermsAPI Service can not be null")

    override val description: ModuleDescription = ModuleDescription(
        "ScoreboardListener",
        "1.0",
        "DevSnox",
        "Definiert die Darstellung der Tablist!",
        true
    )

    override fun start() {
        this.prepareTablist()
        Utils.goThroughAllPlayers { this.setTablist(it) }
    }

    private fun prepareTablist() {
        this.scoreboard.teams.forEach { it.unregister() }

        this.luckPermsApi.groupManager.loadedGroups.forEach { group ->
            val edit = StringBuilder()


            if (group.name != "default") {
                edit.append(ChatColor.translateAlternateColorCodes('&', group.friendlyName))
                edit.append(" §8| ")
            }

            edit.append("§7")

            this.scoreboard.registerNewTeam(formatGroupWeight(group.name)).prefix =
                    edit.substring(0, if (edit.length < 16) edit.length else 15)
        }
    }

    private fun getTeamForPlayer(p: Player): Team {
        return this.scoreboard.getTeam(
            formatGroupWeight(
                Objects.requireNonNull<User>(
                    this.luckPermsApi.getUser(
                        p.uniqueId
                    )
                ).primaryGroup
            )
        )
    }

    private fun formatGroupWeight(group: String): String {
        val weight = Objects.requireNonNull<Group>(this.luckPermsApi.getGroup(group)).weight
        return (if (weight.isPresent) if (weight.asInt <= 100) 100 - weight.asInt else 100 else 100).toString()
    }

    private fun setTablist(player: Player) {
        val team = getTeamForPlayer(player)
        if (!team.hasEntry(player.name)) team.addEntry(player.name)
        player.scoreboard = this.scoreboard
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        this.setTablist(player)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        this.getTeamForPlayer(player).removeEntry(player.name)
    }
}
