/*
 * © Copyright - Lars Artmann aka. LartyHD 2018.
 */

import com.google.gson.JsonArray
import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService.loadAs
import net.darkdevelopers.darkbedrock.darkness.general.functions.asString
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 02.12.2018 21:28.
 * Current Version: 1.0 (02.12.2018 - 22.12.2018)
 */
class NoHungerModule : Module, Listener(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)) {

    override val description: ModuleDescription =
        ModuleDescription(javaClass.canonicalName, "1.0.1", "Lars Artmann | LartyHD", "")

    private val worlds: List<String> by lazy {
        val configData = ConfigData(description.folder, "worlds.json")
        val jsonArray = loadAs(configData) ?: JsonArray()
        jsonArray.mapNotNull { it.asString() }
    }

    @EventHandler
    fun onFoodLevelChangeEvent(event: FoodLevelChangeEvent) {
        val player = event.entity as? Player ?: return
        if (event.foodLevel > player.foodLevel) return
        if (worlds.isNotEmpty() && player.world.name !in worlds) return
        event.cancel()
    }

}