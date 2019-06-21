/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.minecraft.servercore.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import de.astride.minecraft.servercore.common.ServerCore
import java.nio.file.Path

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
class ServerCoreVelocityPlugin @Inject private constructor(@DataDirectory private val path: Path) {

    @Subscribe
    fun on(event: ProxyInitializeEvent) {
        ServerCore(path.toFile())
    }

}