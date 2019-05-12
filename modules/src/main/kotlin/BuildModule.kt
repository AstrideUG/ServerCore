import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.SimplePlayerCommandModule
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import org.bukkit.command.CommandSender
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 05.05.2019 15:38.
 * Current Version: 1.0 (05.05.2019 - 05.05.2019)
 */
class BuildModule : SimplePlayerCommandModule("ToggleBuild") {

    private val javaPlugin: ServerCoreSpigotPlugin = JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)
    private var listener: Listener? = null
    override val command: () -> PlayerCommand = { PlayerCommand(javaPlugin) }

    override fun execute(sender: CommandSender, target: Player) {

        if (target.hasMetadata(key)) target.removeMetadata(key, javaPlugin)
        else target.setMetadata(key, FixedMetadataValue(javaPlugin, true))
    }

    override fun start() {
        super.start()
        listener = BuildListener(javaPlugin)
    }

    override fun stop() {
        super.stop()
        listener?.unregister()
    }


    class BuildListener(javaPlugin: JavaPlugin) : Listener(javaPlugin) {

        @EventHandler(priority = EventPriority.HIGHEST)
        fun on(event: BlockPlaceEvent) = check(event.player, event)

        @EventHandler(priority = EventPriority.HIGHEST)
        fun on(event: BlockBreakEvent) = check(event.player, event)

        @EventHandler(priority = EventPriority.HIGHEST)
        fun on(event: PlayerInteractEvent) = check(event.player, event)

        @EventHandler(priority = EventPriority.HIGHEST)
        fun on(event: InventoryClickEvent) = check(event.whoClicked, event)

        private fun check(player: HumanEntity, cancellable: Cancellable) {
            if (player.hasMetadata(key)) cancellable.cancel(false)
        }

    }

    companion object {
        private const val key: String = "build"
    }

}