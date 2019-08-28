package de.astride.servercore.modules.norain

import com.google.gson.JsonArray
import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.toConfigData
import net.darkdevelopers.darkbedrock.darkness.general.functions.toList
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toBukkitWorld
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.event.world.WorldLoadEvent

/**
 * @author Lars Artmann | LartyHD
 * Created on 05.07.2018 11:17.
 */
class NoRainModule : Module, EventsTemplate() {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.5.0",
        "Lars Artmann | LartyHD",
        "This modules block rain"
    )

    private val worlds by lazy {
        val configData = description.folder.toConfigData("worlds")
        val worlds = configData.load<JsonArray>().toList()
        worlds.mapNotNull { it?.toString() }.ifEmpty { Bukkit.getWorlds().map { it.name } }
    }

    override fun start() {

        val plugin = ServerCoreSpigotPlugin.javaPlugin
        worlds.forEach { it.toBukkitWorld()?.clearWeather() }

        listen<WeatherChangeEvent>(plugin) { event -> if (event.world.name in worlds) event.cancel(event.toWeatherState()) }.add()
        listen<WorldLoadEvent>(plugin) { event -> if (event.world.name in worlds) event.world.clearWeather() }.add()

    }

    override fun stop(): Unit = reset()

    private fun World.clearWeather() {
        setStorm(false)
        isThundering = false
    }

}