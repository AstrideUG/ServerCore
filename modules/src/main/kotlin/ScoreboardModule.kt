/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.functions.toNonNull
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.Utils
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.index
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.sendScoreBoard
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.toScoreboardScore
import net.milkbowl.vault.economy.Economy
import net.minecraft.server.v1_8_R3.ScoreboardScore
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 28.12.2018 11:59.
 * Current Version: 1.0 (28.12.2018 - 15.02.2019)
 */
class ScoreboardModule : Module, Listener(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)) {

    companion object {
        private val economy
            get() = Bukkit.getServicesManager()?.getRegistration(Economy::class.java)?.provider.toNonNull("Vault Economy")
    }

    override val description: ModuleDescription = ModuleDescription(
        javaClass.canonicalName,
        "1.0-SNAPSHOT",
        "DevSnox & Lars Artmann | LartyHD",
        "Handles the scoreboard!"
    )

    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        this.sendScoreboards()
    }

    @EventHandler
    fun onPlayerDisconnectEvent(event: PlayerDisconnectEvent) {
        this.sendScoreboards(Bukkit.getOnlinePlayers().size - 1)
    }

    fun Player.generateScoreboard(size: Int): List<ScoreboardScore> = mutableListOf(
        " ",
        "${ChatColor.GREEN}${ChatColor.BOLD}Online",
        "$size ${ChatColor.GRAY}/${ChatColor.RESET} ${Bukkit.getMaxPlayers()}",
        "  ",
        "${ChatColor.YELLOW}${ChatColor.BOLD}Gold",
        economy.format(economy.getBalance(player)),
        "   ",
        "${ChatColor.DARK_AQUA}${ChatColor.BOLD}Discord",
        "discord.cosmicsky.net",
        "    "
    ).index().toScoreboardScore()

    fun Player.sendScoreboard(size: Int) = sendScoreBoard(
        player,
        "${ChatColor.AQUA}${ChatColor.BOLD}CosmicSky${ChatColor.WHITE}${ChatColor.BOLD}.${ChatColor.AQUA}${ChatColor.BOLD}net",
        player.generateScoreboard(size)
    )

    fun sendScoreboards(size: Int = Bukkit.getOnlinePlayers().size) =
        Utils.goThroughAllPlayers { it.sendScoreboard(size) }

}