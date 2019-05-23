/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.entityid

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.unregister
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import java.util.*

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 20:03.
 * Last edit 23.05.2019
 */
class EntityID : Module, Command(
    ServerCoreSpigotPlugin.javaPlugin,
    EntityID::class.simpleName.toString(),
    "${EntityID::class.simpleName.toString()}.use"
) {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.0.0",
        " Lars Artmann | LartyHD ",
        "Addes a EntityID command"
    )

    private val listener: MutableSet<Listener> = mutableSetOf()
    private val active: MutableSet<UUID> = mutableSetOf()

    private var Player.isActive: Boolean
        get() = uniqueId in active
        set(value) = if (value) active += uniqueId else active -= uniqueId

    override fun start() {
        listener += listen<PlayerInteractEntityEvent>(javaPlugin) { event ->
            if (!event.player.isActive) return@listen
            event.player.sendMessage("Entity id: ${event.rightClicked.entityId}")
        }
        listener += listen<PlayerDisconnectEvent>(javaPlugin) { event ->
            event.player.isActive = false
        }
    }

    override fun stop() {
        listener.unregister()
        listener.clear()
    }

    override fun perform(sender: CommandSender, args: Array<String>): Unit = sender.isPlayer {
        it.isActive = true
        it.sendMessage("EntityID is now active")
    }

}