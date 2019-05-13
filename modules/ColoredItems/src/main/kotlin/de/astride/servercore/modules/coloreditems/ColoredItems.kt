import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 15.02.2019 09:33.
 * Current Version: 1.0 (15.02.2019 - 15.02.2019)
 */
class ColoredItems : Module, Command(
    JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java),
    "ColoredItems",
    "coloreditems.use",
    minLength = 0,
    aliases = * arrayOf("coloritem", "coloreditem")
) {

    override val description: ModuleDescription =
        ModuleDescription(javaClass.canonicalName, "1.0", "Lars Artmann | LartyHD", "")

    private val prefix = "§7[§3Colored§bItem§7] "

    override fun perform(sender: CommandSender, args: Array<String>) = sender.isPlayer { player ->
        if (player.itemInHand.type != Material.AIR) {
            val name = ChatColor.translateAlternateColorCodes('&', args.joinToString(" "))
            ItemBuilder(player.itemInHand).setName(name)
            player.sendMessage("$prefix§7Das Item heißt nun §8\"§r$name§8\"")
        } else player.sendMessage("$prefix§cDu musst ein Item in der Hand halten")
    }

}