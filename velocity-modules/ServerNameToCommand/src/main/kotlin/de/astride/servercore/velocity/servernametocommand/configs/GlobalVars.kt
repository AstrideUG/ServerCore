/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand.configs

import com.google.gson.JsonObject
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigMap
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.save
import net.darkdevelopers.darkbedrock.darkness.general.functions.toConfigData
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import java.io.File
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

/*
 * Created on 23.06.2019 05:24.
 * @author Lars Artmann | LartyHD
 */

lateinit var messages: Messages
lateinit var permissions: Permissions
lateinit var config: Config

@ExperimentalStdlibApi
fun createConfigs(directory: File): Unit = setOf(
    ::config,
    ::permissions,
    ::messages
).createConfigs(directory)


@ExperimentalStdlibApi
fun Iterable<KMutableProperty0<*>>.createConfigs(directory: File): Unit = forEach { property ->

    val configData = property.name.toLowerCase().toConfigData(directory)
    val values = configData.load<JsonObject>().toMap()

    val createType = property.returnType.jvmErasure
    val constructor = createType.constructors.find {
        it.parameters.singleOrNull()?.type == typeOf<Map<String, Any?>>()
    } ?: return@forEach

    val instance = constructor.call(values)
    property.setter.call(instance)

    configData.save(instance.toConfigMap())

}



