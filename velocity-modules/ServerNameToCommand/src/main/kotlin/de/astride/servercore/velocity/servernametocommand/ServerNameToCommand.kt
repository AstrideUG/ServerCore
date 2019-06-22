/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand

import com.google.gson.JsonObject
import de.astride.minecraft.servercore.velocity.ServerCoreVelocityPlugin
import de.astride.servercore.velocity.servernametocommand.commands.ConnectCommand
import de.astride.servercore.velocity.servernametocommand.configs.Messages
import de.astride.servercore.velocity.servernametocommand.holder.messages
import net.darkdevelopers.darkbedrock.darkness.general.configs.defaultMappings
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigMap
import net.darkdevelopers.darkbedrock.darkness.general.functions.save
import net.darkdevelopers.darkbedrock.darkness.general.functions.toConfigData
import net.darkdevelopers.darkbedrock.darkness.general.functions.toJsonElement
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.kyori.text.Component
import net.kyori.text.serializer.ComponentSerializers

/**
 * Created on 21.06.2019 23:17.
 * @author Lars Artmann | LartyHD
 */
class ServerNameToCommand : Module {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.0.0",
        "Lars Artmann | LartyHD",
        ""
    )

    override fun start() {

        val configData = "messages".toConfigData(description.folder)

        defaultMappings += Component::class.java to { any ->
            ComponentSerializers.JSON.deserialize(any.toString())
        }

        val jsonObject = configData.toJsonElement() as? JsonObject ?: JsonObject()
        messages = Messages(jsonObject.toMap())
        configData.save(messages.toConfigMap())

        val server = ServerCoreVelocityPlugin.server
        server.allServers.forEach {
            server.commandManager.register(ConnectCommand(it), it.serverInfo.name)
        }

    }

}
