package de.astride.servercore.modules.buildmode

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin.Companion.javaPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.SimplePlayerCommandModule
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.unregister
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventPriority.HIGHEST
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.Metadatable

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 05.05.2019 15:38.
 * Current Version: 1.0 (05.05.2019 - 13.05.2019)
 */
class BuildModeModule : SimplePlayerCommandModule("ToggleBuild") {

    private val listener: MutableSet<Listener> = mutableSetOf()
    override val command: () -> PlayerCommand = { PlayerCommand(javaPlugin) }

    override fun execute(sender: CommandSender, target: Player) {
        if (target.hasMetadata(key)) target.removeMetadata(key, javaPlugin)
        else target.setMetadata(key, FixedMetadataValue(javaPlugin, true))
    }

    override fun start() {
        listener += listen<BlockPlaceEvent>(javaPlugin, priority = HIGHEST) { it.player.check(it) }
        listener += listen<BlockBreakEvent>(javaPlugin, priority = HIGHEST) { it.player.check(it) }
        listener += listen<PlayerInteractEvent>(javaPlugin, priority = HIGHEST) { it.player.check(it) }
        listener += listen<InventoryClickEvent>(javaPlugin, priority = HIGHEST) { it.whoClicked.check(it) }
    }

    override fun stop() {
        listener.unregister()
        listener.clear()
    }

    companion object {
        private const val key: String = "build"

        private fun Metadatable.check(cancellable: Cancellable) {
            if (hasMetadata(key)) cancellable.cancel(false)
        }
    }

}