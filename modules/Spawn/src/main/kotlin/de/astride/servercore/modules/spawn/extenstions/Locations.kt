/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.servercore.modules.spawn.extenstions

import net.darkdevelopers.darkbedrock.darkness.spigot.location.extensions.toBukkitLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultLivingLocation
import org.bukkit.entity.Entity

/*
 * Created on 24.06.2019 11:54.
 * @author Lars Artmann | LartyHD
 */

fun Collection<DefaultLivingLocation>.teleportToRandom(entity: Entity) {
    val location: DefaultLivingLocation = if (isEmpty()) return else random()
    entity.teleport(location.toBukkitLocation())
}
