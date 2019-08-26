/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn.configs.commands

import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue
import net.darkdevelopers.darkbedrock.darkness.spigot.configs.commands.AbstractCommandConfig

/**
 * Created on 25.06.2019 03:08.
 * @author Lars Artmann | LartyHD
 */
class AddSpawnCommandConfig(values: Map<String, Any?>) : AbstractCommandConfig(values) {

    override val commandName by values.default { "AddSpawn" }
    override val permission by values.default { "modules.spawn.commands.${commandName.toLowerCase()}" }

    val messageAddSpawnTeleportationSuccess: String? by values.default { "The spawn will be added" }
    val messageAddSpawnTeleportationSuccessfully: String? by values.default { "The have been added" }

}