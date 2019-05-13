package de.astride.servercore.modules.clear

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.SimplePlayerCommandModule
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 13.05.2019 05:55.
 * Current Version: 1.0 (13.05.2019 - 13.05.2019)
 */
class ClearModule : SimplePlayerCommandModule("clear") {

    override val command: () -> PlayerCommand = {
        PlayerCommand(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java))
    }

    override fun execute(sender: CommandSender, target: Player) {
        target.inventory.clear()
        target.inventory.armorContents = null
    }
}