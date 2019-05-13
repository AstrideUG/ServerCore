package de.astride.servercore.modules.feed

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.SimplePlayerCommandModule
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.12.2018 03:00.
 * Current Version: 1.0 (22.12.2018 - 15.02.2019)
 */
class FeedModule : SimplePlayerCommandModule("Feed") {

    override val command: () -> SimplePlayerCommandModule.PlayerCommand = {
        PlayerCommand(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java))
    }

    override fun execute(sender: CommandSender, target: Player) {
        target.foodLevel = 20
        target.saturation = 20F
    }

}