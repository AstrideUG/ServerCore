/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn.commands

import de.astride.servercore.modules.spawn.configs.commands.SpawnCommandConfig
import de.astride.servercore.modules.spawn.configs.config
import de.astride.servercore.modules.spawn.extenstions.teleportToRandom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.darkdevelopers.darkbedrock.darkness.spigot.configs.commands.extensions.register
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.isPlayer
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.schedule
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.TimeUnit

/**
 * Created on 24.06.2019 09:13.
 * @author Lars Artmann | LartyHD
 */
object SpawnCommand {

    fun setup(
        plugin: JavaPlugin,
        values: Map<String, Any?>
    ) {
        SpawnCommandConfig(values).register(plugin) { sender, _, commandConfig ->
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
    }

}