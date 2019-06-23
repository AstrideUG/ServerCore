/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand

import com.velocitypowered.api.proxy.server.RegisteredServer
import de.astride.minecraft.servercore.velocity.ServerCoreVelocityPlugin
import de.astride.minecraft.servercore.velocity.ServerCoreVelocityPlugin.Companion.logger
import de.astride.servercore.velocity.servernametocommand.commands.ConnectCommand
import de.astride.servercore.velocity.servernametocommand.configs.config
import de.astride.servercore.velocity.servernametocommand.configs.createConfigs
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription

/**
 * Created on 21.06.2019 23:17.
 * @author Lars Artmann | LartyHD
 */
class ServerNameToCommand : Module {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.0.0",
        "Lars Artmann | LartyHD",
        "Adds all sub server as command and server groups"
    )

    @ExperimentalStdlibApi
    override fun start() {

        createConfigs(description.folder)

        val server = ServerCoreVelocityPlugin.server
        val allServers = server.allServers
        allServers.forEach { server.commandManager.register(ConnectCommand(setOf(it), logger), it.serverInfo.name) }
        val rawCategories = allServers.map { it.serverInfo.name.split(config.splitter).first() to it }

        val categories = mutableMapOf<String, MutableSet<RegisteredServer>>()
        for ((key, value) in rawCategories) categories.getOrPut(key) { mutableSetOf() } += value

        categories.forEach { (category, servers) ->
            server.commandManager.register(ConnectCommand(servers, logger), category)
        }

    }

}
