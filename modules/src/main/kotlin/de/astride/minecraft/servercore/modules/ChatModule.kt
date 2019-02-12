package de.astride.minecraft.servercore.modules

import me.lucko.luckperms.api.Contexts
import me.lucko.luckperms.api.LuckPermsApi
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * Copyright (c) 2018 DevSnox
 * GitHub: https://github.com/DevSnox
 * Web: http://devsnox.me
 * Mail: me.devsnox@gmail.com
 * Discord: DevSnox#4884 | Skype: live:chaos3729
 */
class ChatModule : Module, Listener {

    private var luckPermsApi: LuckPermsApi? = null

    override val description: ModuleDescription
        get() = ModuleDescription("ChatModule", "1.0", "DevSnox", "Definiert die Darstellung der Nachrichten!", true)

    override fun start() {
        registerLuckPerms()
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("DarkFrame"))
    }

    private fun registerLuckPerms() {
        val provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi::class.java)
        if (provider != null) {
            this.luckPermsApi = provider.provider
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player

        var message = event.message

        if (!player.hasPermission("skypvp.chat.color.all")) {
            if (player.hasPermission("skypvp.chat.color")) {
                message = ChatColor.translateAlternateColorCodes('&', message)
                message = message.replace("\u00A7((?i)[fk-or])".toRegex(), "")
            } else {
                message.replace("&", "")
            }
        }

        val stringBuilder = StringBuilder()

        val group = luckPermsApi!!.getGroup(luckPermsApi!!.getUser(player.uniqueId)!!.primaryGroup)


        val prefix =
            luckPermsApi!!.userManager.getUser(player.uniqueId)!!.cachedData.getMetaData(Contexts.allowAll()).prefix

        stringBuilder.append(prefix).append("§8- ").append(prefix!!.substring(0, 2)).append(player.name).append(" §8» ")

        when (group!!.name) {
            "owner", "admin" -> stringBuilder.append("§4§l")
            "developer" -> stringBuilder.append("§3§l")
            else -> {
                if (player.hasPermission("skyhype.chat.team")) {
                    stringBuilder.append("§e§l")
                }

                stringBuilder.append("§7")
            }
        }

        stringBuilder.append(message.replace("%", "%%"))

        event.format = ChatColor.translateAlternateColorCodes('&', stringBuilder.toString())
    }
}