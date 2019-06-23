/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand.configs

import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue

/**
 * Created on 23.06.2019 05:46.
 * @author Lars Artmann | LartyHD
 */
class Config(values: Map<String, Any?>) {

    val splitter by values.default { "-" }

}