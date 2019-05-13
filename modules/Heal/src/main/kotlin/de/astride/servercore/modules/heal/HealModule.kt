package de.astride.servercore.modules.heal

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.SimplePlayerCommandModule
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.12.2018 04:46.
 * Current Version: 1.0 (22.12.2018 - 15.02.2019)
 */
class HealModule : SimplePlayerCommandModule("Heal") {

    override val command: () -> SimplePlayerCommandModule.PlayerCommand = {
        PlayerCommand(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java))
    }

    override fun execute(sender: CommandSender, target: Player) {
        target.foodLevel = 20
        target.saturation = 20F
        target.health = 20.0
        target.activePotionEffects.forEach { target.removePotionEffect(it.type) }
    }

}