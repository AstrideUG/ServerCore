/*
 * © Copyright - Lars Artmann aka. LartyHD 2018.
 */

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toWorlds
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
        ModuleDescription(javaClass.canonicalName, "1.0", "Lars Artmann | LartyHD", "")

    private lateinit var worlds: List<String>

    override fun load() {
        worlds = try {
            GsonService.loadAsJsonArray(ConfigData(description.folder, "worlds.json"))
                .mapNotNull { it.asJsonPrimitive.asString }
        } catch (ex: ClassCastException) {
            emptyList()
        }
    }

    @EventHandler
    fun on(event: FoodLevelChangeEvent) {
        val worlds = worlds.toWorlds()
        if (event.foodLevel > (event.entity as Player).foodLevel) return
        if (!worlds.isEmpty() && event.entity.world !in worlds) return
        cancel(event)
    }
}