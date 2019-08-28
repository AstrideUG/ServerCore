/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn.configs

import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultLivingLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.lookableLocationOf
import net.darkdevelopers.darkbedrock.darkness.spigot.location.vector.inmutable.extensions.vector3Of
import org.bukkit.Bukkit

/**
 * Created on 24.06.2019 09:13.
 * @author Lars Artmann | LartyHD
 */
class Config(values: Map<String, Any?>) {

    val locations: MutableList<DefaultLivingLocation> by values.default {
        mutableListOf(lookableLocationOf(Bukkit.getWorlds().first().name, vector3Of(0.0, 100.0, 0.0), 0f, 0f))
    }

}