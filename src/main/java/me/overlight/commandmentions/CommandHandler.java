package me.overlight.commandmentions;

import me.overlight.commandmentions.Actions.Action;
import me.overlight.commandmentions.Actions.impl.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.util.HashMap;

public class CommandHandler implements Listener {
    @EventHandler
    public void event(PlayerCommandPreprocessEvent e) {
        if(CommandMentions.Global.getWhitelists().contains(e.getPlayer().getName())) return;
        if(CommandMentions.Global.isBypassEnabled() && e.getPlayer().hasPermission(CommandMentions.Global.getBypassPermission())) return;
        for(CommandCheck check: CommandCheck.checks){
            if(check.getWhitelists().contains(e.getPlayer().getName())) continue;
            boolean f = false;
            for(CommandCheck.CheckMode c: check.getCheckModes()) {
                switch (c) {
                    case contains:
                        f = check.getCommands().stream().anyMatch(q -> e.getMessage().contains(q));
                        break;
                    case endwith:
                        f = check.getCommands().stream().anyMatch(q -> e.getMessage().endsWith(q));
                        break;
                    case startwith:
                        f = check.getCommands().stream().anyMatch(q -> e.getMessage().startsWith(q));
                        break;
                }
            }
            if(f) {
                if(check.hasAction(Block.class)){
                    e.setCancelled(true);
                }
                for(Action act: check.getActions().keySet()){
                    HashMap<String, String> r = new HashMap<>();
                    r.put("%player%", e.getPlayer().getName());
                    act.onExecute(check.getActions().get(act), r);
                }
                if(check.getDiscordWebhooks() == null || check.getDiscordWebhooks().isEmpty()) return;
                check.getDiscordWebhooks().forEach(webhook -> {
                    try {
                        new DiscordWebhook(webhook).addEmbed(new DiscordWebhook.EmbedObject().addField("Username", e.getPlayer().getName(), true)
                                .addField("Command", e.getMessage(), true).setTitle("Command Execute").setColor(check.getDisEmbedColor()).setAuthor("CommandMentions", null, null)).execute();
                    } catch (IOException ex) { }
                });
            }
        }
    }
}
