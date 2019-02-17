import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.SimplePlayerCommandModule
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by DevSnox on 12.02.18
 * Copyright (c) 2018 DevSnox
 * GitHub: https://github.com/DevSnox
 * Web: http://devsnox.me
 * Mail: me.devsnox@gmail.com
 * Discord: DevSnox#4884 | Skype: live:chaos3729
 */
class SkullModule : SimplePlayerCommandModule("skull") {

    override val command: () -> SimplePlayerCommandModule.PlayerCommand = {
        PlayerCommand(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java))
    }

    override fun execute(sender: CommandSender, target: Player) {
        target.inventory.addItem(ItemBuilder(Material.SKULL_ITEM, 3.toShort()).setOwner(target.name).build())
    }
}