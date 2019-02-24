import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 24.02.2019 22:48.
 * Current Version: 1.0 (24.02.2019 - 24.02.2019)
 */
class EntityInfos : Module, Command(
    JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java),
    "EntityInfos",
    "EntityInfos.use"
) {

    override val description: ModuleDescription =
        ModuleDescription(javaClass.canonicalName, "1.0", " Lars Artmann | LartyHD ", "Addes a EntityInfo command")

    override fun perform(sender: CommandSender, args: Array<String>) {

        sender.sendMessage("Worlds:")
        Bukkit.getWorlds().forEach { world ->

            sender.sendMessage("    Entities in world ${world.name} (${world.entities.size}):")
            world.entities.forEach { sender.sendMessage("        ${it.name}") }
            sender.sendMessage("")
            sender.sendMessage("        Types:")
            EntityType.values().forEach { type ->
                sender.sendMessage("        ${type.name}:${world.entities.filter { it.type == type }.size}")
            }
        }

        sender.sendMessage("")
        sender.sendMessage("")
        sender.sendMessage("")

        sender.sendMessage("Counts:")
        sender.sendMessage("    Worlds: ${Bukkit.getWorlds().size}")
        sender.sendMessage("    Entities: ${Bukkit.getWorlds().flatMap { it.entities }.size}")

    }

}