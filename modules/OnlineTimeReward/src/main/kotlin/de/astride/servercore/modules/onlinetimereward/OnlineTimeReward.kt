package de.astride.servercore.modules.onlinetimereward

import de.astride.minecraft.servercore.spigot.ServerCoreSpigotPlugin
import net.darkdevelopers.darkbedrock.darkness.general.functions.toNonNull
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.Utils.players
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable


/**
 * @authors Lars Artmann | LartyHD - Yasin Dalal (DevSnox)
 * Created by Lars Artmann | LartyHD on 15.02.2019 09:54.
 * Current Version: 1.0 (15.02.2019 - 15.02.2019)
 */
class OnlineTimeReward : Module {

    override val description: ModuleDescription =
        ModuleDescription(
            javaClass.canonicalName,
            "1.1",
            "Lars Artmann | LartyHD and Yasin Dalal (DevSnox)",
            "Passives Einkommen der Spieler"
        )


    private val economy: Economy = Bukkit.getServer().servicesManager.getRegistration<Economy>(Economy::class.java)
        ?.provider.toNonNull("Economy can not be null")

    private val runnable: BukkitRunnable = object : BukkitRunnable() {
        override fun run() {
            players.forEach { ({ economy.depositPlayer(it, 5.0) })() }
        }
    }

    override fun start() {
        runnable.runTaskTimerAsynchronously(JavaPlugin.getPlugin(ServerCoreSpigotPlugin::class.java), 0L, 1200L)
    }

    override fun stop() = runnable.cancel()

}