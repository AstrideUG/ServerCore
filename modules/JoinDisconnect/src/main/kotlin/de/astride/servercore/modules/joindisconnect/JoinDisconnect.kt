/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.joindisconnect


import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonConfig
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.setDisconnectMessage
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.setJoinMessage
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.unregisterDisconnectMessage
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.unregisterJoinMessage
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.SpigotGsonMessages

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 28.12.2018 09:42 (moved on 13.05.2019 at 06:05 from DarkBedrock).
 * Current Version: 1.0 (13.05.2019 - 13.05.2019)
 */
class JoinDisconnect : Module {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.0.0",
        "Lars Artmann | LartyHD and DevSnox",
        "Adds Join and Disconnect messages"
    )

    override fun start() {
        val configData = ConfigData(description.folder, "messages.json")
        @Suppress("DEPRECATION")
        val messages = SpigotGsonMessages(GsonConfig(configData).load()).availableMessages
        setJoinMessage { event ->
            val player = event.player ?: return@setJoinMessage null
            if (!player.hasPlayedBefore())
                messages["Join.First"]?.joinToString("\n")?.replace("<Player>", player.name, true)
            else messages["Join"]?.joinToString("\n")?.replace("<Player>", player.name, true)
        }
        setDisconnectMessage { messages["Disconnect"]?.joinToString("\n")?.replace("<Player>", it.player.name, true) }
    }

    override fun stop() {
        unregisterJoinMessage()
        unregisterDisconnectMessage()
    }

}
