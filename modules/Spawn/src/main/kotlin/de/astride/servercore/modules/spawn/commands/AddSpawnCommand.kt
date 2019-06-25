/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn.commands

import de.astride.servercore.modules.spawn.configs.commands.AddSpawnCommandConfig
import net.darkdevelopers.darkbedrock.darkness.spigot.configs.commands.extensions.register
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.isPlayer
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.lookableLocationOf
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created on 24.06.2019 09:12.
 * @author Lars Artmann | LartyHD
 */
@Suppress("unused")
object AddSpawnCommand {

    fun setup(
        plugin: JavaPlugin,
        values: Map<String, Any?>/*,
        directory: File*/
    ) {
        AddSpawnCommandConfig(values).register(plugin) { sender, _, commandConfig ->
            sender.isPlayer {
                fun Float.round() = (this * 100).toInt() / 100F

                commandConfig.messageAddSpawnTeleportationSuccess?.sendIfNotNull(sender)

                val location = it.location.clone()
                /* config.locations +=*/ lookableLocationOf(
                location.world.name,
                location.blockX + 0.5,
                location.y,
                location.blockZ + 0.5,
                location.yaw.round(),
                location.pitch.round()
            )

//                ::config.toConfigData(directory).save(config.toConfigMap())

                commandConfig.messageAddSpawnTeleportationSuccessfully?.sendIfNotNull(sender)
            }
        }
    }

}