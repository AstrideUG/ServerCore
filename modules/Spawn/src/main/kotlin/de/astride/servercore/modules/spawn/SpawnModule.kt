/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin.Companion.javaPlugin
import de.astride.servercore.modules.spawn.commands.registerAddSpawnCommand
import de.astride.servercore.modules.spawn.commands.registerSpawnCommand
import de.astride.servercore.modules.spawn.configs.config
import de.astride.servercore.modules.spawn.configs.configs
import de.astride.servercore.modules.spawn.configs.directory
import de.astride.servercore.modules.spawn.configs.save
import de.astride.servercore.modules.spawn.extenstions.teleportToRandom
import net.darkdevelopers.darkbedrock.darkness.general.configs.createConfigs
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Created on 23.12.2018 16:53.
 * @author Lars Artmann | LartyHD
 */
class SpawnModule : Module, EventsTemplate() {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.0.3",
        "Lars Artmann | LartyHD",
        "Adds a spawn command"
    )

    override fun start() {

        directory = description.folder

        configs.createConfigs(directory)

        registerSpawnCommand(javaPlugin)
        registerAddSpawnCommand(javaPlugin)

        listen<PlayerJoinEvent>(javaPlugin) {
            config.locations.teleportToRandom(it.player)
        }.add()

    }

    override fun stop() {
        configs.map { it.save() }
        reset()
    }

}