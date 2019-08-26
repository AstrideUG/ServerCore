/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn.commands

import de.astride.servercore.modules.spawn.configs.addSpawnCommandConfig
import de.astride.servercore.modules.spawn.configs.config
import de.astride.servercore.modules.spawn.configs.save
import de.astride.servercore.modules.spawn.configs.spawnCommandConfig
import de.astride.servercore.modules.spawn.extenstions.teleportToRandom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.darkdevelopers.darkbedrock.darkness.spigot.configs.commands.extensions.register
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.isPlayer
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.schedule
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.lookableLocationOf
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.TimeUnit

/**
 * Created on 26.08.2019 19:51.
 * @author Lars Artmann | LartyHD
 */
fun registerSpawnCommand(plugin: JavaPlugin) = spawnCommandConfig.register(plugin) { sender, _, commandConfig ->
    sender.isPlayer { player ->
        fun String?.hold(timeUnit: TimeUnit) = this?.replace(
            "<Delay.${timeUnit.name}>",
            timeUnit.convert(commandConfig.delay, TimeUnit.MILLISECONDS).toString(),
            true
        )

        var s = commandConfig.messageSpawnTeleportationSuccess
        TimeUnit.values().forEach { timeUnit -> s = s.hold(timeUnit) }
        s?.sendIfNotNull(sender)

        GlobalScope.launch {
            delay(commandConfig.delay)
            plugin.schedule {
                config.locations.teleportToRandom(player)
                commandConfig.messageSpawnTeleportationSuccessfully.sendIfNotNull(sender)
            }
        }

    }
}

fun registerAddSpawnCommand(plugin: JavaPlugin) = addSpawnCommandConfig.register(plugin) { sender, _, commandConfig ->
    sender.isPlayer {
        fun Float.round() = (this * 100).toInt() / 100F

        commandConfig.messageAddSpawnTeleportationSuccess?.sendIfNotNull(sender)

        val location = it.location.clone()

        config.locations += lookableLocationOf(
            location.world.name,
            location.blockX + 0.5,
            location.y,
            location.blockZ + 0.5,
            location.yaw.round(),
            location.pitch.round()
        )

        ::config.save()

        commandConfig.messageAddSpawnTeleportationSuccessfully?.sendIfNotNull(sender)
    }
}