package de.astride.servercore.modules.chat

import me.lucko.luckperms.api.Contexts
import me.lucko.luckperms.api.LuckPermsApi
import net.darkdevelopers.darkbedrock.darkness.general.functions.toNonNull
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.setChatFormat
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.unregisterChatFormat
import org.bukkit.Bukkit
import org.bukkit.ChatColor

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 13.05.2019 04:38.
 * Current Version: 1.0 (13.05.2019 - 13.05.2019)
 */
class ChatModule : Module {

    private val luckPermsApi: LuckPermsApi
        get() = Bukkit.getServicesManager().getRegistration(LuckPermsApi::class.java)?.provider.toNonNull("LuckPermsAPI Service can not be null")

    override val description: ModuleDescription = ModuleDescription(
        javaClass.canonicalName,
        "1.0.0",
        "DevSnox & Lars Artmann | LartyHD",
        "Definiert die Darstellung der Nachrichten!"
    )

    override fun start() = setChatFormat { event ->
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
            "developer" -> stringBuilder.append("§f§l")
            else -> {
                if (player.hasPermission("chatmodule.color.team")) stringBuilder.append("§e§l")
                else stringBuilder.append("§7")
            }
        }

        ChatColor.translateAlternateColorCodes('&', stringBuilder.toString())
    }

    override fun stop() = unregisterChatFormat()

}