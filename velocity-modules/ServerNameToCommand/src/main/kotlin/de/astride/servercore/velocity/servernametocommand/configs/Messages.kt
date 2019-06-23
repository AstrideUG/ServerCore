/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand.configs

import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue

/**
 * Created on 21.06.2019 23:49.
 * @author Lars Artmann | LartyHD
 */
class Messages(values: Map<String, Any?>) {

    val onlyForPlayers by values.default { """{"text":"Only for players!","color":"red"}""" }
    val youWillSendToServer by values.default { """["",{"text":"You will send to","color":"aqua"},{"text":" @server@"},{"text":"...","color":"aqua"}]""" }

}