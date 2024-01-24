package me.overlight.commandmentions;

import me.overlight.commandmentions.Actions.Action;
import me.overlight.commandmentions.Actions.impl.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.awt.*;
import java.util.HashMap;

public class CommandHandler implements Listener {
    @EventHandler
    public void event(PlayerCommandPreprocessEvent e) {
        for(CommandCheck check: CommandCheck.checks){
            boolean f = false;
            switch(check.getCheckMode()) {
                case contains: f = check.getCommands().stream().anyMatch(q -> e.getMessage().contains(q)); break;
                case endwith: f = check.getCommands().stream().anyMatch(q -> e.getMessage().endsWith(q)); break;
                case startwith: f = check.getCommands().stream().anyMatch(q -> e.getMessage().startsWith(q)); break;
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
                if(check.getDiscordWebhook() == null || check.getDiscordWebhook().isEmpty()) return;
                try {
                    new DiscordWebhook(check.getDiscordWebhook()).addEmbed(new DiscordWebhook.EmbedObject().addField("Username", e.getPlayer().getName(), true)
                            .addField("Command", e.getMessage(), true).setTitle("Command Execute").setColor(check.getDisEmbedColor()).setAuthor("CommandMentions", null, null)).execute();
                } catch (Exception ex) { }
            }
        }
    }
}
