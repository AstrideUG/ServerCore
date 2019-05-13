/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.tpa

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin.Companion.javaPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.SimplePlayerCommandModule
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.unregister
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.*

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 24.12.2018 18:21. (moved on 13.05.2019 at 06:20 from DarkBedrock)
 * Current Version: 1.0 (24.12.2018 - 24.12.2018)
 */
class TPA : SimplePlayerCommandModule("Tpa") {

    private var listener: Listener? = null

    override val command: () -> SimplePlayerCommandModule.PlayerCommand = {
        PlayerCommand(
            javaPlugin,
            usage = "<Player> [accept]",
            minLength = 1,
            maxLength = 2
        )
    }
    private val map = mutableMapOf<UUID, MutableList<UUID>>() //Sender, MutableList<Target>

    override fun start() {
        listener = listen<PlayerDisconnectEvent>(javaPlugin) { map.remove(it.player.uniqueId) }
    }

    override fun stop() {
        listener?.unregister()
        listener = null
    }

    override fun execute(sender: CommandSender, target: Player, args: Array<String>) {
        if (sender === target) config.messages["$prefix.SendToHimSelf.Failed"]?.sendIfNotNull(sender)
        else sender.isPlayer { player ->
            when {
                args.size == 2 -> if (args[1] == "accept") {
                    val list = map[target.uniqueId] ?: mutableListOf()
                    if (player.uniqueId in list) {
                        if (list.size < 2) map.remove(player.uniqueId) else map[target.uniqueId]?.minusAssign(player.uniqueId)
                        if (target.isOnline) {
                            "$prefix.Accept.Sender.Success".sendReplaced(sender, player, target)
                            "$prefix.Accept.Target.Success".sendReplaced(target, player, target)
                            player.teleport(target)
                            "$prefix.Accept.Sender.Successfully".sendReplaced(sender, player, target)
                            "$prefix.Accept.Target.Successfully".sendReplaced(target, player, target)
                        } else config.messages["$prefix.Accept.Failed.TargetIsNotOnline"]?.sendIfNotNull(sender)
                    } else config.messages["$prefix.Accept.Failed.IsNotInTheMap"]?.sendIfNotNull(sender)
                } else commandInstance.sendUseMessage(sender)
                target.uniqueId in map[player.uniqueId] ?: mutableListOf() ->
                    config.messages["$prefix.Add.Failed.IsAlreadyInTheMap"]?.sendIfNotNull(sender)
                else -> {
                    //TextComponentSerializer.
                    "$prefix.Add.Sender.Success".sendReplaced(sender, player, target)
                    "$prefix.Add.Target.Success".sendReplaced(target, player, target)
                    map.putIfAbsent(target.uniqueId, mutableListOf())?.plusAssign(player.uniqueId)
                    "$prefix.Add.Sender.Successfully".sendReplaced(sender, player, target)
                    "$prefix.Add.Target.Successfully".sendReplaced(target, player, target)
                }
            }
        }
    }

    private fun String.sendReplaced(sender: CommandSender, player: Player, target: Player) {
        config.messages[this]?.map {
            it?.replace("<Player>", player.name, true)
                ?.replace("<Target>", target.name, true)

        }?.sendIfNotNull(sender)
    }

}