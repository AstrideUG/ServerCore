/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.itemconvertingoninteraction

import com.google.gson.JsonObject
import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import de.astride.servercore.modules.itemconvertingoninteraction.config.Config
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigMap
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.save
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.configs.initSpigotStaticConfigMappings
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.removeItemInHand
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerInteractEvent

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 11.06.2019 00:27.
 * Last edit 11.06.2019
 */
class ItemConvertingOnInteraction : Module, EventsTemplate() {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.simpleName,
        "1.0.0",
        "Lars Artmann | LartyHD",
        "Changes the item in hand on interaction"
    )

    override fun start() {
        val configData = ConfigData(description.folder, "config")
        val config = Config(configData.load<JsonObject>().toMap())
        configData.save(config.toConfigMap())
        val materials = config.materials

        initSpigotStaticConfigMappings()

        listen<PlayerInteractEvent>(ServerCoreSpigotPlugin.javaPlugin) { event ->
            materials.forEach {
                if (it.item != event.item) return@forEach
                if (it.block != event.clickedBlock) return@forEach
                val player = event.player ?: return@forEach
                player.removeItemInHand()
                player.inventory.addItem(it.outPutItem)
                event.clickedBlock?.type = config.replacement

                event.setUseItemInHand(Event.Result.DENY)
                event.setUseInteractedBlock(Event.Result.DENY)
            }
        }.add()
    }

    override fun stop(): Unit = reset()

}