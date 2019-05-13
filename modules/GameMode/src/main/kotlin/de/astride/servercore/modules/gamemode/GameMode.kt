package de.astride.servercore.modules.gamemode

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.functions.getOrKey
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.SimplePermissionsCommandModule
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.possiblePlayer
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toGameMode
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.12.2018 05:47.
 * Current Version: 1.0 (22.12.2018 - 15.02.2019)
 */
class GameMode : SimplePermissionsCommandModule("de.astride.servercore.modules.gamemode.GameMode") {
    private val otherPerms get() = config.permissions.getOrKey("$singlePerms.Other")
    override val command: () -> PermissionCommand = {
        object : PermissionCommand(
            JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java),
            usage = "<$defaultCommandName>:$singlePerms|<$defaultCommandName> [Player]:$otherPerms",
            minLength = 1,
            maxLength = 2,
            tabCompleter = TabCompleter { _, _, _, args ->
                when (args.size) {
                    1 -> GameMode.values().map { it.name }
                    2 -> Bukkit.getOnlinePlayers().map { it.name }
                    else -> listOf<String>()
                }
            },
            aliases = *arrayOf("GM", "Mode")
        ) {
            override fun perform(sender: CommandSender, args: Array<String>) {
                val gameMode = args[0].toGameMode()
                if (gameMode == null) sendUseMessage(sender)
                else {
                    possiblePlayer(
                        config.messages,
                        prefix,
                        sender,
                        if (args.size == 1) null else args[1],
                        singlePerms,
                        otherPerms
                    ) { _: CommandSender, target: Player -> target.gameMode = gameMode }
                }
            }
        }
    }

    private fun Map<String, List<String?>>.getOrKey(key: String): String =
        map { it.key to it.value.firstOrNull() }.toMap().getOrKey(key)
}