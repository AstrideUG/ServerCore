/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand.configs

import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.kyori.text.TextComponent

/**
 * Created on 21.06.2019 23:49.
 * @author Lars Artmann | LartyHD
 */
class Messages(values: Map<String, Any?>) {

    val noPermission by values.default { TextComponent.of("You have no permissions") }
    val onlyForPlayers by values.default { TextComponent.of("Only for players") }


}