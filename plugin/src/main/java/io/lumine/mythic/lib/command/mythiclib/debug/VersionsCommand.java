package io.lumine.mythic.lib.command.mythiclib.debug;

import io.lumine.mythic.lib.command.api.CommandTreeNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class VersionsCommand extends CommandTreeNode {
    public VersionsCommand(CommandTreeNode parent) {
        super(parent, "versions");
    }

    @Override
    public CommandResult execute(CommandSender sender, String[] args) {

        System.out.println("Plugin versions:");
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            System.out.print("> " + plugin.getName() + ": " + plugin.getDescription().getVersion());
            if (!Bukkit.getPluginManager().isPluginEnabled(plugin))
                System.out.print(" (Disabled)");
            System.out.println();
        }

        sender.sendMessage("Plugin versions pasted to your server console");

        return CommandResult.SUCCESS;
    }
}
