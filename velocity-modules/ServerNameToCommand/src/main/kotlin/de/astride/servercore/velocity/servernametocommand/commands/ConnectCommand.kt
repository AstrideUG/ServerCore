/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand.commands

import com.velocitypowered.api.command.Command
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import de.astride.servercore.velocity.servernametocommand.configs.config
import de.astride.servercore.velocity.servernametocommand.configs.messages
import de.astride.servercore.velocity.servernametocommand.configs.permissions
import de.astride.servercore.velocity.servernametocommand.functions.toComponent
import java.util.logging.Logger

/**
 * Created on 21.06.2019 23:37.
 * @author Lars Artmann | LartyHD
 */
class ConnectCommand(private val servers: Set<RegisteredServer>, private val logger: Logger) : Command {

    init {
        if (servers.isEmpty()) throw IllegalArgumentException("servers can not be empty")
    }

    override fun execute(source: CommandSource, args: Array<out String>) {
        if (source is Player) {
            val server = servers.random()
            val component =
                messages.youWillSendToServer.replace("@server@", server.serverInfo.name.toString(), true).toComponent()
            source.sendMessage(component)
            source.createConnectionRequest(server).fireAndForget()
        } else source.sendMessage(messages.onlyForPlayers.toComponent())
    }

    override fun hasPermission(source: CommandSource, args: Array<out String>): Boolean {
        val player = source as? Player
        if (config.disableOnCurrentServer && player?.currentServer?.orElse(null)?.server in servers) return false
        return servers.any { source.hasPermission(permissions.connectCommand.serverName(it)) }
    }

    private fun String.serverName(it: RegisteredServer): String =
        replace("@servers@", it.serverInfo.name.replace(config.splitter, ".", true), true)

}