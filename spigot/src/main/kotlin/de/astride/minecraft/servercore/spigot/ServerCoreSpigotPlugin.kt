package de.astride.minecraft.servercore.spigot

import de.astride.minecraft.servercore.common.ServerCore
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 12.01.2019 19:17.
 * Current Version: 1.0 (12.01.2019 - 13.05.2019)
 */
class ServerCoreSpigotPlugin : DarkPlugin() {

    override fun onEnable() = onEnable {
        ServerCore(dataFolder)
    }

    companion object {
        val javaPlugin: ServerCoreSpigotPlugin get() = getPlugin(ServerCoreSpigotPlugin::class.java)
    }

}