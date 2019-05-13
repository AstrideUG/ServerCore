/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.scoreboard

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonConfig
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.unregister
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendScoreBoard
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.SpigotGsonMessages
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.Utils.players
import org.bukkit.ChatColor.*
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 28.12.2018 11:59.
 * Current Version: 1.1.0 (28.12.2018 - 13.05.2019)
 */
class ScoreboardModule : Module, EventsTemplate() {

    private val messagesKey: String = "messages.json"

    override val description: ModuleDescription = ModuleDescription(
        javaClass.canonicalName,
        "1.0.0",
        "Lars Artmann | LartyHD",
        "Adds a side ScoreBoard"
    )

    private lateinit var displayName: String
    private lateinit var entries: Set<String>

    override fun start() {

        val configData = ConfigData(description.folder, messagesKey)
        @Suppress("DEPRECATION")
        val spigotGsonMessages = SpigotGsonMessages(GsonConfig(configData).load())
        val availableMessages = spigotGsonMessages.availableMessages

        if (availableMessages.isEmpty()) {
            val output = javaClass.getResourceAsStream("default${File.separator}$messagesKey").reader().readText()
            GsonService.save(configData, output)
        }

        displayName = availableMessages["display-name"]?.joinToString("")
            ?: "$AQUA${BOLD}CraftPlugin$WHITE$BOLD.$AQUA${BOLD}net"
        entries = availableMessages["entries"]?.mapNotNull { it }?.toSet()
            ?: setOf("", "powered by $AQUA${BOLD}CraftPlugin$WHITE$BOLD.$AQUA${BOLD}net")


        val plugin: ServerCoreSpigotPlugin = JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java)
        registerListener(plugin)
    }

    override fun stop() {
        listener.unregister()
        listener.clear()
    }

    private fun registerListener(plugin: Plugin) {
        listen<PlayerJoinEvent>(plugin) { sendScoreboards() }.add()
        listen<PlayerDisconnectEvent>(plugin) { sendScoreboards() }.add()
    }

    private fun sendScoreboards(): Unit = players.forEach {
        it.sendScoreBoard(displayName, entries)
    }

}