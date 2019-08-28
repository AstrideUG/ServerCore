package de.astride.servercore.modules.moveborder

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import de.astride.servercore.modules.moveborder.configs.config
import net.darkdevelopers.darkbedrock.darkness.general.configs.createConfig
import net.darkdevelopers.darkbedrock.darkness.general.message.MultiLanguageMessages
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.*
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import net.darkdevelopers.darkbedrock.darkness.spigot.location.extensions.toBukkitLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*

class MoveBorder : Module, EventsTemplate() {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.0.0",
        "LeBaasti",
        "Teleports players to given location if they are out of area"
    )

    override fun start() {
        ::config.createConfig(description.folder)

        listen<PlayerMoveEvent>(ServerCoreSpigotPlugin.javaPlugin){ event->
            val player = event.player ?: return@listen
            val location = config.locations.random()
            if (player.world.name != location.world || event.to.y in config.minY..config.maxY) return@listen
            config.messageTeleportBackSuccess.sendIfNotNull(player)
            player.teleport(location.toBukkitLocation())
            config.messageTeleportBackSuccessfully.sendIfNotNull(player)
        }.add()
    }

    override fun stop() = reset()

}