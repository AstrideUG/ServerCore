import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.SimplePermissionsCommandModule
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by DevSnox on 12.02.18
 * Copyright (c) 2018 DevSnox
 * GitHub: https://github.com/DevSnox
 * Web: http://devsnox.me
 * Mail: me.devsnox@gmail.com
 * Discord: DevSnox#4884 | Skype: live:chaos3729
 */
class TrashModule : SimplePermissionsCommandModule("trash") {

    override val command: () -> SimplePermissionsCommandModule.PermissionCommand = {
        object : PermissionCommand(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)) {
            override fun perform(sender: CommandSender, args: Array<String>) {
                sender.isPlayer {
                    it.openInventory(Bukkit.createInventory(it, 27, config.messages["inventory-name"]))
                }
            }
        }
    }
}