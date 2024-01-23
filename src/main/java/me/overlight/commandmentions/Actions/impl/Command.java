package me.overlight.commandmentions.Actions.impl;

import me.overlight.commandmentions.Actions.Action;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Command extends Action {
    public Command() {
        super("command");
    }

    @Override
    public void onExecute(String args, HashMap<String, String> placeholders) {
        AtomicReference<String> f = new AtomicReference<>(args);
        placeholders.forEach((key, value) -> f.set(f.get().replace(key, value)));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), f.get());
    }
}
