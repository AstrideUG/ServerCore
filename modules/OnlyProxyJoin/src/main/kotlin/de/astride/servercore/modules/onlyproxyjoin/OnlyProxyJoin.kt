/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.onlyproxyjoin

import com.google.gson.JsonObject
import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.toConfigData
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.ChatColor
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerLoginEvent

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 02.06.2019 23:31.
 * Last edit 05.06.2019
 */
class OnlyProxyJoin : Module, EventsTemplate() {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.0.1",
        "Lars Artmann | LartyHD",
        "This module asks if the host address is valid"
    )

    override fun start() {
        val config = Config(description.folder.toConfigData("config").load<JsonObject>().toMap())
        listen<PlayerLoginEvent>(ServerCoreSpigotPlugin.javaPlugin, priority = EventPriority.HIGHEST) { event ->
            if (config.proxyIps.none { it == event.realAddress.hostAddress })
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, config.kickMessage)
        }.add()
    }

    override fun stop(): Unit = reset()

    class Config(values: Map<String, Any?>) {
        val proxyIps by values.default { setOf("0.0.0.0", "127.0.0.1") }
        val kickMessage by values.default { "${ChatColor.RED}Use the proxy to join!" }
    }

}