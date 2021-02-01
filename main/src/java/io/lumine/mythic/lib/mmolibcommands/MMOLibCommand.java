package io.lumine.mythic.lib.mmolibcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MMOLibCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MMOLib.plugin.reload();
        sender.sendMessage(ChatColor.GREEN + "> MMOLib Reloaded!");
        return true;
    }
}