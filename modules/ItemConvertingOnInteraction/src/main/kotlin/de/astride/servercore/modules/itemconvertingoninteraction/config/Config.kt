/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.itemconvertingoninteraction.config

import de.astride.servercore.modules.itemconvertingoninteraction.data.Materials
import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import org.bukkit.Material.*
import org.bukkit.inventory.ItemStack

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 11.06.2019 00:54.
 * Last edit 11.06.2019
 */
class Config(values: Map<String, Any?>) {
    val replacement by values.default { AIR }
    val materials by values.default { setOf(Materials(BUCKET, OBSIDIAN, ItemStack(LAVA_BUCKET))) }
}