package me.overlight.commandmentions;

import lombok.Getter;
import lombok.Setter;
import me.overlight.commandmentions.Actions.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter @Setter
public class CommandCheck {
    public final static List<CommandCheck> checks = new ArrayList<>();
    private final List<String> commands = new ArrayList<>();
    private CheckMode checkMode;
    private String discordWebhook;
    private final HashMap<Action, String> actions = new HashMap<>();

    public boolean hasAction(Class<? extends Action> cls) {
        return actions.keySet().stream().anyMatch(cls::isInstance);
    }

    public enum CheckMode {
        contains, startwith, endwith
    }
}
