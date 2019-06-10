/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.itemconvertingoninteraction.data

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 11.06.2019 00:51.
 * Last edit 11.06.2019
 */
data class Materials(
    val item: Material?,
    val block: Material?,
    val outPutItem: ItemStack?
)
