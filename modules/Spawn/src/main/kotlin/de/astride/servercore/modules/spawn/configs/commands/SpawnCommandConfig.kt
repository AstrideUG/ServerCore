/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn.configs.commands

import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue
import net.darkdevelopers.darkbedrock.darkness.spigot.configs.commands.AbstractCommandConfig

/**
 * Created on 25.06.2019 02:03.
 * @author Lars Artmann | LartyHD
 */
class SpawnCommandConfig(values: Map<String, Any?>) : AbstractCommandConfig(values) {

    override val commandName by values.default { "Spawn" }
    override val permission by values.default { "spawnmodule.commands.spawn" }

    val delay by values.default { 3000L }

    val messageSpawnTeleportationSuccess: String? by values.default { "You will be teleported" }
    val messageSpawnTeleportationSuccessfully: String? by values.default { "You have been teleported" }

}