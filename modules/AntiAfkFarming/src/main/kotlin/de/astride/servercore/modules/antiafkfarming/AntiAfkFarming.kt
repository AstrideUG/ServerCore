package de.astride.servercore.modules.antiafkfarming

/*
 * © Copyright - Lars Artmann aka. LartyHD 2018.
 */

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import me.devsnox.advancedafksystem.api.AdvancedAfkSystem
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 15.02.2019 09:21.
 * Current Version: 1.0 (15.02.2019 - 16.02.2019)
 */
class AntiAfkFarming : Module, Listener(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)) {

    override val description: ModuleDescription =
        ModuleDescription(javaClass.canonicalName, "1.0", "Lars Artmann | LartyHD", "Verhindert Afk farming!")


    @EventHandler
    fun on(event: BlockBreakEvent) {
        val player = event.player
        val afkPlayer = AdvancedAfkSystem.advancedAfkSystemAPI.getAfkPlayer(event.player)
        if (!afkPlayer.isAfk) return
        player.sendMessage("§cDu wurdest als AFK-Farming erkannt. Falls dies ein Fehler ist bewege dich einfach kurz!")
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 3))
    }

}