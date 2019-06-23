/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.velocity.servernametocommand.functions

import net.kyori.text.serializer.gson.GsonComponentSerializer
import net.kyori.text.serializer.legacy.LegacyComponentSerializer

/*
 * Created on 23.06.2019 06:21.
 * @author Lars Artmann | LartyHD
 */

@Suppress("DEPRECATION")
fun String.toComponent() = if (startsWith("[") && endsWith("]"))
    GsonComponentSerializer.INSTANCE.deserialize(this)
else LegacyComponentSerializer.legacyLinking().deserialize(this, '&')