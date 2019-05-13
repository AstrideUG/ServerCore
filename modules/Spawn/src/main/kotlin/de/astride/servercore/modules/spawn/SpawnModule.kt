/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

import com.google.gson.JsonObject
import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin.Companion.javaPlugin
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonConfig
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService.loadAs
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonStringMapWithSubs
import net.darkdevelopers.darkbedrock.darkness.general.functions.getOrKey
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendIfNotNull
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toJsonObject
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toMap
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import net.darkdevelopers.darkbedrock.darkness.spigot.location.toBukkitLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.location.toLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.location.toMap
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.SpigotGsonMessages
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.*
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.event.world.WorldLoadEvent
import java.util.concurrent.TimeUnit

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.12.2018 16:53. (moved on 13.05.2019 at 04:47 from DarkBedrock)
 * Current Version: 1.0 (13.05.2019 - 13.05.2019)
 */
class SpawnModule : Module {

    override val description: ModuleDescription = ModuleDescription(
        javaClass.canonicalName,
        "1.0.0",
        "Lars Artmann | LartyHD",
        "Adds SetSpawn and Spawn command"
    )

    private lateinit var config: Config
    private lateinit var listener: org.bukkit.event.Listener
    private var location: Location? = null

    override fun load() {
        config = Config()
        location = config.getLocation()
    }

    override fun start() {
        SpawnCommand()
        SetSpawnCommand()
        listener = SpawnListener()
    }


    private inner class Config {

        val spawnKey = "spawn"
        val configData = ConfigData(description.folder, "config.json")
        val jsonObject = loadAs(configData) ?: JsonObject()
        val spawnElement = jsonObject.getOrJsonObject(spawnKey)
        val spawn = GsonConfig.multiPlaceJsonObject(spawnElement, spawnKey, configData.directory)
        @Suppress("DEPRECATION")
        val messages = SpigotGsonMessages(GsonConfig(configData).load()).availableMessages
        val permissions = GsonStringMapWithSubs(jsonObject.getAsOrJsonObject("permissions")).available
        val commandNames = GsonStringMapWithSubs(jsonObject.getAsOrJsonObject("command-names")).available
        val delay = jsonObject["delay"]?.asLong ?: 3000

        fun saveLocation(location: Location) {
            val jsonElement = location.toLocation().toMap().toJsonObject(jsonObject = spawn)
            val (a, b) = if (spawnElement.isJsonPrimitive)
                ConfigData(description.folder, spawnElement.asString) to jsonElement
            else {
                jsonObject.add(spawnKey, jsonElement)
                configData to jsonObject
            }
            GsonService.save(a, b)
        }

        fun getLocation(): Location = spawn.toMap().toLocation().toBukkitLocation()

    }

    private inner class SpawnCommand : Command(
        javaPlugin,
        config.commandNames.getOrKey("Spawn"),
        config.permissions.getOrKey("SpawnNewModule.Command.Spawn")
    ) {

        override fun perform(sender: CommandSender, args: Array<String>) = sender.isPlayer {
            fun String?.delay(timeUnit: TimeUnit) = this?.replace(
                "<Delay.${timeUnit.name}>",
                timeUnit.convert(config.delay, TimeUnit.MILLISECONDS).toString(),
                true
            )

            if (location == null) {
                location = config.getLocation()
                if (location == null) {
                    config.messages["Spawn.Teleportation.Failed"]?.sendIfNotNull(sender)
                    return@isPlayer
                }
            }

            var s = config.messages["Spawn.Teleportation.Success"]
            TimeUnit.values().forEach { timeUnit -> s = s?.map { it?.delay(timeUnit) } }
            s?.sendIfNotNull(sender)

            GlobalScope.launch {
                delay(config.delay)
                Bukkit.getScheduler().runTask(javaPlugin) {
                    it.teleport(location)
                    config.messages["Spawn.Teleportation.Successfully"]?.sendIfNotNull(sender)
                }
            }


        }

    }

    private inner class SetSpawnCommand : Command(
        javaPlugin,
        config.commandNames.getOrKey("SetSpawn"),
        config.permissions.getOrKey("SpawnNewModule.Command.SetSpawn")
    ) {

        override fun perform(sender: CommandSender, args: Array<String>) = sender.isPlayer {
            fun Float.round() = (this * 100).toInt() / 100F

            config.messages["Spawn.Set.Success"]?.sendIfNotNull(sender)

            val cloned = it.location.clone()
            val location = Location(
                cloned.world,
                cloned.blockX + 0.5,
                cloned.y,
                cloned.blockZ + 0.5,
                cloned.yaw.round(),
                cloned.pitch.round()
            )

            config.saveLocation(location)
            this@SpawnModule.location = location

            config.messages["Spawn.Set.Successfully"]?.sendIfNotNull(sender)
        }

    }

    private inner class SpawnListener : Listener(javaPlugin) {

        private val prefix = "Spawn.Events.Bypass."

        init {
            location?.world?.apply {
                clearWeather()
                setGameRuleValue("keepInventory", true.toString())
                setGameRuleValue("doDaylightCycle", false.toString())
                time = 6000
            }
        }

        @EventHandler
        fun on(event: BlockBreakEvent) = block(event, event.player)

        @EventHandler
        fun on(event: BlockPlaceEvent) = block(event, event.player)

        @EventHandler
        fun on(event: PlayerPickupItemEvent) = block(event)

        @EventHandler
        fun on(event: PlayerDropItemEvent) = block(event)

        @EventHandler
        fun on(event: PlayerBucketFillEvent) = block(event)

        @EventHandler
        fun on(event: PlayerBucketEmptyEvent) = block(event)

        @EventHandler
        fun on(event: PlayerJoinEvent) {
            val player = event.player
            if (!player.hasPlayedBefore() && location != null) player.teleport(location)
            player.changeGameMode(event)
        }

        @EventHandler
        fun on(event: PlayerChangedWorldEvent) = event.player.changeGameMode(event)

        @EventHandler
        fun on(event: EntityDamageEvent) {
            if (event.entity is Player) block(event, event.entity as Player)
        }

        @EventHandler
        fun on(event: FoodLevelChangeEvent) {
            val player = event.entity as Player
            if (event.foodLevel < player.foodLevel) block(event, player)
        }

        @EventHandler
        fun on(event: PlayerInteractEvent) {
            if (event.action != Action.PHYSICAL) return
            block(event)
        }

        @EventHandler
        fun on(event: WorldLoadEvent) = event.world.clearWeather()

        @EventHandler
        fun on(event: WeatherChangeEvent) = check(event.world) {
            event.cancel(event.toWeatherState())
        }

        @EventHandler
        fun on(event: PlayerMoveEvent) {
            val player = event.player ?: return
            val location = location ?: return
            if (player.world != location.world || event.to.y in 0.0..300.0) return
            config.messages["Spawn.Events.Move.Success"]?.sendIfNotNull(player)
            player.teleport(location)
            config.messages["Spawn.Events.Move.Successfully"]?.sendIfNotNull(player)
        }

        private fun block(event: PlayerEvent) = block(event, event.player)

        private fun block(event: Event, player: Player) = check(event, player) { (event as Cancellable).cancel() }

        private fun Player.changeGameMode(event: Event) = check(event, this) { gameMode = GameMode.ADVENTURE }

        private inline fun check(event: Event, player: Player, block: () -> Unit) = check(player.world) {
            if (!player.checkPerm(event.permissionsKey())) block()
        }

        private inline fun check(world: World, block: () -> Unit) {
            if (world == location?.world) block()
        }

        private fun CommandSender.checkPerm(permissionsKey: String): Boolean {
            return hasPermission(config.permissions[permissionsKey]?.joinToString("") ?: return false)
        }

        private fun Event.permissionsKey() = "$prefix${eventName.removeSuffix("Event")}"

        private fun World.clearWeather() = check(this) {
            setStorm(false)
            isThundering = false
        }

    }

    private fun JsonObject.getOrJsonObject(key: String) = this.get(key) ?: JsonObject()

    private fun JsonObject.getAsOrJsonObject(key: String) = this.getAsJsonObject(key) ?: JsonObject()


    fun Map<String, List<String?>>.getOrKey(key: String): String =
        map { it.key to it.value.firstOrNull() }.toMap().getOrKey(key)

}