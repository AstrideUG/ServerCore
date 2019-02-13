package de.astride.minecraft.servercore.modules;

import me.lucko.luckperms.api.LuckPermsApi;
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module;
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Consumer;

/**
 * Created by DevSnox on 12.02.18
 * Copyright (c) 2018 DevSnox
 * GitHub: https://github.com/DevSnox
 * Web: http://devsnox.me
 * Mail: me.devsnox@gmail.com
 * Discord: DevSnox#4884 | Skype: live:chaos3729
 */
public final class TablistModule implements Module, Listener {

    private Scoreboard scoreboard;
    private LuckPermsApi luckPermsApi;

    @NotNull
    @Override
    public ModuleDescription getDescription() {
        return new ModuleDescription("ScoreboardListener", "1.0", "DevSnox", "Definiert die Darstellung der Tablist!", true);
    }

    @Override
    public void load() {

    }

    @Override
    public void start() {
        this.registerLuckPerms();
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("DarkFrame"));
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.prepareTablist();

        Bukkit.getOnlinePlayers().forEach((Consumer<Player>) this::setTablist);
    }

    @Override
    public void stop() {

    }

    private void registerLuckPerms() {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider != null) {
            this.luckPermsApi = provider.getProvider();
        }
    }

    private void prepareTablist() {
        this.scoreboard.getTeams().forEach(Team::unregister);

        this.luckPermsApi.getGroupManager().getLoadedGroups().forEach(group -> {
            final StringBuilder edit = new StringBuilder();


            if (!group.getName().equals("default")) {
                edit.append(ChatColor.translateAlternateColorCodes('&', group.getFriendlyName()));
                edit.append(" §8| ");
            }

            edit.append("§7");

            this.scoreboard.registerNewTeam(formatGroupWeight(group.getName())).setPrefix(edit.substring(0, edit.length() < 16 ? edit.length() : 15));
        });
    }

    private Team getTeamForPlayer(Player p) {
        return this.scoreboard.getTeam(formatGroupWeight(Objects.requireNonNull(this.luckPermsApi.getUser(p.getUniqueId())).getPrimaryGroup()));
    }

    private String formatGroupWeight(String group) {
        OptionalInt weight = Objects.requireNonNull(this.luckPermsApi.getGroup(group)).getWeight();
        return String.valueOf(weight.isPresent() ? (weight.getAsInt() <= 100 ? 100 - weight.getAsInt() : 100) : 100);
    }

    private void setTablist(Player player) {
        Team team = getTeamForPlayer(player);
        if (!team.hasEntry(player.getName())) team.addEntry(player.getName());
        player.setScoreboard(this.scoreboard);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.setTablist(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.getTeamForPlayer(player).removeEntry(player.getName());
    }
}
