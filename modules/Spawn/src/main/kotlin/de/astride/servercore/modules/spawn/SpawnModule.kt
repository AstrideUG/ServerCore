/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin.Companion.javaPlugin
import de.astride.servercore.modules.spawn.commands.SpawnCommand
import de.astride.servercore.modules.spawn.configs.config
import de.astride.servercore.modules.spawn.extenstions.teleportToRandom
import net.darkdevelopers.darkbedrock.darkness.general.configs.createConfig
import net.darkdevelopers.darkbedrock.darkness.general.configs.mapFromConfig
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigMap
import net.darkdevelopers.darkbedrock.darkness.general.functions.save
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Created on 23.12.2018 16:53.
 * @author Lars Artmann | LartyHD
 */
class SpawnModule : Module {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.0.2",
        "Lars Artmann | LartyHD",
        "Adds a spawn command"
    )

    private lateinit var listener: Listener
    private val directory get() = description.folder

    override fun start() {

        ::config.createConfig(directory)

        SpawnCommand.setup(javaPlugin, SpawnCommand.javaClass.mapFromConfig(directory))
//        AddSpawnCommand.setup(javaPlugin, AddSpawnCommand.javaClass.mapFromConfig(directory))

        listener = listen<PlayerJoinEvent>(javaPlugin) {
            config.locations.teleportToRandom(it.player)
        }

    }

    override fun stop() {
        ::config.toConfigData(directory).save(config.toConfigMap())
    }

}