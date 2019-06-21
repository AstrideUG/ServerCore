/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.minecraft.servercore.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginDescription
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import de.astride.minecraft.servercore.common.ServerCore
import java.nio.file.Path
import java.util.logging.Logger

/**
 * Created on 21.06.2019 22:55.
 * @author Lars Artmann | LartyHD
 */
@Plugin(
    id = "servercore",
    name = "ServerCore",
    version = "@version@",
    authors = ["Lars Artmann | LartyHD"]
)
class ServerCoreVelocityPlugin @Inject private constructor(
    server: ProxyServer,
    logger: Logger,
    @DataDirectory path: Path,
    description: PluginDescription
) {

    init {
        Companion.server = server
        Companion.logger = logger
        Companion.path = path
        Companion.description = description
    }

    @Subscribe
    fun on(event: ProxyInitializeEvent) {
        ServerCore(path.toFile())
    }

    companion object {
        lateinit var server: ProxyServer
        lateinit var logger: Logger
        lateinit var path: Path
        lateinit var description: PluginDescription
    }

}