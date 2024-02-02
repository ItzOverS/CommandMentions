package me.overlight.commandmentions;

import lombok.Getter;
import lombok.Setter;
import me.overlight.commandmentions.Actions.Action;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter @Setter
public class CommandCheck {
    public final static List<CommandCheck> checks = new ArrayList<>();
    private final List<String> commands = new ArrayList<>();
    private List<CheckMode> checkModes = new ArrayList<>();
    private Color disEmbedColor;
    private List<String> discordWebhooks = new ArrayList<>();
    private final List<String> whitelists = new ArrayList<>();
    private final HashMap<Action, String> actions = new HashMap<>();

    public boolean hasAction(Class<? extends Action> cls) {
        return actions.keySet().stream().anyMatch(cls::isInstance);
    }

    public enum CheckMode {
        contains, startwith, endwith
    }
}
