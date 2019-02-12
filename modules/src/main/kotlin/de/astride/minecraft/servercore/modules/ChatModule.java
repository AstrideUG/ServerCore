package de.astride.minecraft.servercore.modules;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import net.darkdevelopers.darkbedrock.darkness.general.modules.Module;
import net.darkdevelopers.darkbedrock.darkness.general.modules.ModuleDescription;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Copyright (c) 2018 DevSnox
 * GitHub: https://github.com/DevSnox
 * Web: http://devsnox.me
 * Mail: me.devsnox@gmail.com
 * Discord: DevSnox#4884 | Skype: live:chaos3729
 */
public final class ChatModule implements Module, Listener {

    private LuckPermsApi luckPermsApi;

    @NotNull
    @Override
    public ModuleDescription getDescription() {
        return new ModuleDescription("ChatModule", "1.0", "DevSnox", "Definiert die Darstellung der Nachrichten!", true);
    }

    @Override
    public void load() {

    }

    @Override
    public void start() {
        registerLuckPerms();
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("DarkFrame"));
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        String message = event.getMessage();

        if (!player.hasPermission("skypvp.chat.color.all")) {
            if (player.hasPermission("skypvp.chat.color")) {
                message = ChatColor.translateAlternateColorCodes('&', message);
                message = message.replaceAll("\u00A7((?i)[fk-or])", "");
            } else {
                message.replace("&", "");
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        Group group = luckPermsApi.getGroup(luckPermsApi.getUser(player.getUniqueId()).getPrimaryGroup());


        String prefix = luckPermsApi.getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData(Contexts.allowAll()).getPrefix();

        stringBuilder.append(prefix).append("§8- ").append(prefix.substring(0, 2)).append(player.getName()).append(" §8» ");

        switch (group.getName()) {
            case "owner":
            case "admin":
                stringBuilder.append("§4§l");
                break;
            case "developer":
                stringBuilder.append("§3§l");
                break;
            default:
                if (player.hasPermission("skyhype.chat.team")) {
                    stringBuilder.append("§e§l");
                    break;
                }

                stringBuilder.append("§7");
                break;
        }

        stringBuilder.append(message.replace("%", "%%"));

        event.setFormat(ChatColor.translateAlternateColorCodes('&', stringBuilder.toString()));
    }
}