package me.overlight.commandmentions;

import lombok.Getter;
import me.overlight.commandmentions.Actions.Action;
import me.overlight.commandmentions.Actions.impl.Block;
import me.overlight.commandmentions.Actions.impl.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandMentions extends JavaPlugin {
    @Getter private static CommandMentions instance;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        getServer().getPluginManager().registerEvents(new CommandHandler(), this);
        getServer().getPluginCommand("cmrl").setExecutor(new ReloadCmd());
        loadData();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static class Global {
        @Getter
        private static List<String> whitelists = new ArrayList<>();
        @Getter
        private static boolean BypassEnabled;
        @Getter
        private static String BypassPermission;
    }
    public static void loadData() {
        getInstance().reloadConfig();
        CommandCheck.checks.clear();
        FileConfiguration f = getInstance().getConfig();
        { // Global
            Global.whitelists = f.getStringList("global.whitelists");
            Global.BypassEnabled = f.getBoolean("global.bypass.enabled");
            Global.BypassPermission = Global.BypassEnabled? "commandmentions" + f.getString("global.bypass.permission"): null;
        }
        for(String key: f.getKeys(false)){
            if(key.equals("global")) continue;
            if(!f.isConfigurationSection(key)) continue;
            CommandCheck c = new CommandCheck();
            ConfigurationSection q = f.getConfigurationSection(key);
            c.getCheckModes().addAll(q.isList("check-mode")? (q.getStringList("check-mode").stream().map(r -> CommandCheck.CheckMode.valueOf(r.toLowerCase())).collect(Collectors.toList())): Collections.singletonList(CommandCheck.CheckMode.valueOf((q.getString("check-mode", "contains").toLowerCase()))));
            c.getDiscordWebhooks().addAll(q.isList("discord-webhook")? q.getStringList("discord-webhook"): Collections.singletonList(q.getString("discord-webhook")));
            c.setDisEmbedColor(Color.getColor(q.getString("discord-webhook", "gray")));
            c.getCommands().addAll(q.isList("command")? q.getStringList("command"): Collections.singletonList(q.getString("command")));
            q.getStringList("actions").forEach(r -> {
                Action a = null;
                switch(r.split("]")[0].substring(1).toLowerCase()){
                    case "command": a = new Command(); break;
                    case "block": a = new Block(); break;
                }
                if(a == null) return;
                c.getActions().put(a, r.substring(r.split("]")[0].length() + 1));
            });
            CommandCheck.checks.add(c);
        }
    }
}
