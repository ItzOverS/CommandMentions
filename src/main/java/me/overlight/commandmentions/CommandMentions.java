package me.overlight.commandmentions;

import lombok.Getter;
import me.overlight.commandmentions.Actions.Action;
import me.overlight.commandmentions.Actions.impl.Block;
import me.overlight.commandmentions.Actions.impl.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

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

    public static void loadData() {
        getInstance().reloadConfig();
        CommandCheck.checks.clear();
        FileConfiguration f = getInstance().getConfig();
        for(String key: f.getKeys(false)){
            CommandCheck c = new CommandCheck();
            ConfigurationSection q = f.getConfigurationSection(key);
            c.setCheckMode(CommandCheck.CheckMode.valueOf((q.getString("check-mode", "contains").toLowerCase())));
            c.setDiscordWebhook(q.getString("discord-webhook", null));
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
