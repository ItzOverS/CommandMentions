package me.overlight.commandmentions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("commandmentions.reload")) return false;
        CommandMentions.loadData();
        commandSender.sendMessage("§eConfiguration successfully reloaded!");
        return false;
    }
}
