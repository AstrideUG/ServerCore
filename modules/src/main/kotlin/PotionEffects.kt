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
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 15.02.2019 08:37.
 * Current Version: 1.0 (15.02.2019 - 15.02.2019)
 */
class PotionEffects : Module, Listener(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)) {

    override val description: ModuleDescription =
        ModuleDescription(javaClass.canonicalName, "1.0", "Lars Artmann | LartyHD", "")

    private lateinit var worlds: List<String>
    private val effects = GsonService.loadAsJsonArray(ConfigData(description.folder, "effects.json")).map {
        val effect = it.asJsonObject
        val type = try {
            @Suppress("DEPRECATION")
            PotionEffectType.getById(effect["effect"]!!.asInt)
        } catch (e: Exception) {
            PotionEffectType.getByName(effect["effect"]!!.asString)
        }
        val duration = effect["duration"]?.asInt ?: Int.MAX_VALUE
        val amplifier = effect["amplifier"]?.asInt ?: 0
        val ambient = effect["ambient"]?.asBoolean ?: true
        val particles = effect["has-particles"]?.asBoolean ?: true
        PotionEffect(type, duration, amplifier, ambient, particles)
    }

    override fun load() {
        worlds = try {
            GsonService.loadAsJsonArray(ConfigData(description.folder, "worlds.json"))
                .mapNotNull { it.asJsonPrimitive.asString }
        } catch (ex: ClassCastException) {
            emptyList()
        }
    }

    @EventHandler
    fun on(event: PlayerJoinEvent) {
        if (event.player.world !in worlds.toWorlds()) return
        event.player.addPotionEffects(effects)
    }

}