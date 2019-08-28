package de.astride.servercore.modules.moveborder.configs

import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultLivingLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.lookableLocationOf
import net.darkdevelopers.darkbedrock.darkness.spigot.location.vector.inmutable.extensions.vector3Of
import org.bukkit.Bukkit

class Config(values: Map<String, Any?>) {

    val locations: Set<DefaultLivingLocation> by values.default {
        setOf(lookableLocationOf(Bukkit.getWorlds().first().name, vector3Of(0.0, 100.0, 0.0), 0f, 0f))
    }
    val minY by values.default { 0.0 }
    val maxY by values.default { 300.0 }
    val messageTeleportBackSuccess: String? by values.default { "Out of area" }
    val messageTeleportBackSuccessfully: String? by values.default { "" }

}