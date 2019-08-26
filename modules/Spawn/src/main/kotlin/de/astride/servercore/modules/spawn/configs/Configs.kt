/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn.configs

import de.astride.servercore.modules.spawn.configs.commands.AddSpawnCommandConfig
import de.astride.servercore.modules.spawn.configs.commands.SpawnCommandConfig
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigMap
import net.darkdevelopers.darkbedrock.darkness.general.functions.save
import java.io.File
import kotlin.reflect.KMutableProperty0

/*
 * Created on 24.06.2019 09:14.
 * @author Lars Artmann | LartyHD
 */

lateinit var config: Config
lateinit var spawnCommandConfig: SpawnCommandConfig
lateinit var addSpawnCommandConfig: AddSpawnCommandConfig

lateinit var directory: File
val configs = setOf(::config, ::spawnCommandConfig, ::addSpawnCommandConfig)

fun KMutableProperty0<out Any>.save(): Unit =
    toConfigData(directory).save(config.toConfigMap(), serializeNulls = true)
