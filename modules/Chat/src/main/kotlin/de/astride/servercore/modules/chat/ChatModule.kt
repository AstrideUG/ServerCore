package de.astride.servercore.modules.chat

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import me.lucko.luckperms.api.Contexts
import me.lucko.luckperms.api.LuckPermsApi
import net.darkdevelopers.darkbedrock.darkness.general.functions.toNonNull
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * Copyright (c) 2018 DevSnox
 * GitHub: https://github.com/DevSnox
 * Web: http://devsnox.me
 * Mail: me.devsnox@gmail.com
 * Discord: DevSnox#4884 | Skype: live:chaos3729
 */
class ChatModule : Module, Listener(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)) {

    private val luckPermsApi: LuckPermsApi
        get() = Bukkit.getServicesManager().getRegistration(LuckPermsApi::class.java)?.provider.toNonNull("LuckPermsAPI Service can not be null")

    override val description: ModuleDescription =
        ModuleDescription(
            "de.astride.servercore.modules.skull.ChatModule",
            "1.0",
            "DevSnox",
            "Definiert die Darstellung der Nachrichten!",
            true
        )

    @EventHandler(priority = EventPriority.HIGH)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player

        var message = event.message

        if (!player.hasPermission("chatmodule.color.all")) {
            if (player.hasPermission("chatmodule.color.basic")) {
                message = ChatColor.translateAlternateColorCodes('&', message)
                message = message.replace("\u00A7((?i)[fk-or])".toRegex(), "")
            } else {
                message.replace("&", "")
            }
        }

        val stringBuilder = StringBuilder()

        val group = luckPermsApi.getGroup(luckPermsApi.getUser(player.uniqueId)?.primaryGroup ?: "")


        val prefix =
            luckPermsApi.userManager.getUser(player.uniqueId)?.cachedData?.getMetaData(Contexts.allowAll())?.prefix

        stringBuilder.append(prefix).append("§8- ").append(prefix?.substring(0, 2)).append(player.name).append(" §8» ")

        when (group?.name) {
            "owner", "admin" -> stringBuilder.append("§4§l")
            "developer" -> stringBuilder.append("§3§l")
            else -> {
                if (player.hasPermission("chatmodule.color.team")) stringBuilder.append("§e§l")
                else stringBuilder.append("§7")
            }
        }

        stringBuilder.append(message.replace("%", "%%"))

        event.format = ChatColor.translateAlternateColorCodes('&', stringBuilder.toString())
    }
}