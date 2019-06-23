/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand.configs

import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue

/**
 * Created on 23.06.2019 05:25.
 * @author Lars Artmann | LartyHD
 */
class Permissions(values: Map<String, Any?>) {

    val connectCommand by values.default { "servernametocommand.commands.connect.@servername@" }

}