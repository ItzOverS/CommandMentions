package me.overlight.commandmentions.Actions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Action {
    @Getter private final String prefix;

    public Action(String prefix) {
        this.prefix = prefix;
    }

    public void onExecute(String args) { onExecute(args, new HashMap<>()); }
    public void onExecute(String args, HashMap<String, String> placeholders) { }
}
