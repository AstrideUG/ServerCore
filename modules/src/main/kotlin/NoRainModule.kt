import com.google.gson.JsonArray
import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService.loadAs
import net.darkdevelopers.darkbedrock.darkness.general.functions.asString
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 05.07.2018 11:17.
 * Last edit 01.04.2019
 */
class NoRainModule : Module, Listener(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)) {

    override val description: ModuleDescription =
        ModuleDescription(javaClass.canonicalName, "1.3.2", "Lars Artmann | LartyHD", "This modules block rain")

    private val worlds: List<String> by lazy {
        val configData = ConfigData(description.folder, "worlds.json")
        val jsonArray = loadAs(configData) ?: JsonArray()
        jsonArray.mapNotNull { it.asString() }
    }

    @EventHandler
    fun onWeatherChangeEvent(event: WeatherChangeEvent) {
        if (worlds.isEmpty() || event.world.name in worlds) event.cancel(event.toWeatherState())
    }

    @EventHandler
    fun onWorldLoadEvent(event: WorldLoadEvent) = event.world.clearWeather()

    private fun World.clearWeather() {
        setStorm(false)
        isThundering = false
    }

}