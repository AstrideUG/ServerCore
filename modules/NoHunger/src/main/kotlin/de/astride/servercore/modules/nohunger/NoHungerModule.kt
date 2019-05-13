/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.nohunger

import com.google.gson.JsonArray
import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService.loadAs
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.unregister
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toMap
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.region.isInside
import net.darkdevelopers.darkbedrock.darkness.spigot.region.toRegion
import org.bukkit.entity.Player
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 13.05.2019 03:50.
 * Current Version: 1.0 (13.05.2019 - 13.05.2019)
 */
class NoHungerModule : Module, EventsTemplate() {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.canonicalName,
        "1.0.0",
        "Lars Artmann | LartyHD",
        "Block's hunger in the given regions"
    )

    private val regions by lazy {
        val configData = ConfigData(description.folder, "regions.json")
        val jsonArray = loadAs(configData) ?: JsonArray()
        jsonArray.toMap().mapNotNull { it.toRegion() }
    }

    override fun start() {
        val plugin: ServerCoreSpigotPlugin = JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)
        listen<FoodLevelChangeEvent>(plugin) { event ->
            val player = event.entity as? Player ?: return@listen
            if (event.foodLevel > player.foodLevel) return@listen
            if (regions.any { it.isInside(event.entity.location) }) return@listen
            event.cancel()
        }.add()
    }

    override fun stop() {
        listener.unregister()
        listener.clear()
    }

}