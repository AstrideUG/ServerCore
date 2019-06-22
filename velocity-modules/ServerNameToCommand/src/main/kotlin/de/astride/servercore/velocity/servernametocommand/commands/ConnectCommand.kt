/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand.commands

import com.velocitypowered.api.command.Command
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import net.kyori.text.TextComponent

/**
 * Created on 21.06.2019 23:37.
 * @author Lars Artmann | LartyHD
 */
class ConnectCommand(private val server: RegisteredServer) : Command {

    override fun execute(source: CommandSource, args: Array<out String>) {
        if (source.hasPermission("ServerNameToCommand.Connect.${server.serverInfo.name}")) {
            if (source is Player) source.createConnectionRequest(server)
            else source.sendMessage(TextComponent.of("only for Player"))
        } else source.sendMessage(TextComponent.of("no permissions"))
    }

}