package io.lumine.mythic.lib.command.mythiclib.debug;

import gs.mclo.java.APIResponse;
import gs.mclo.java.MclogsAPI;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.command.api.CommandTreeNode;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Scanner;

public class LogsCommand extends CommandTreeNode {
    public LogsCommand(CommandTreeNode parent) {
        super(parent, "logs");
    }

    @Override
    public CommandResult execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You can only use this command as a player");
            return CommandResult.FAILURE;
        }

        try {
            sender.sendMessage("Reading and uploading logs..");
            StringBuilder builder = new StringBuilder();

            // Append plugin versions
            builder.append("Plugin versions:\n");
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                builder.append("> ").append(plugin.getName()).append(" ").append(plugin.getDescription().getVersion()).append(" by ").append(String.join(",", plugin.getDescription().getAuthors()));
                if (!Bukkit.getPluginManager().isPluginEnabled(plugin))
                    builder.append(" (Disabled)");
                builder.append("\n");
            }

            // Append latest log
            builder.append("\nLatest log:\n");
            File log = new File(MythicLib.plugin.getDataFolder(), "..\\..\\logs\\latest.log");
            Scanner scanner = new Scanner(log);
            while (scanner.hasNextLine())
                builder.append(scanner.nextLine()).append("\n");
            scanner.close();

            // Upload everything
            final String logsDir = MythicLib.plugin.getDataFolder().getParentFile().getAbsoluteFile().getParentFile().getAbsoluteFile() + "/logs/";
            final String file = "latest.log";

            APIResponse response = MclogsAPI.share(builder.toString());
            Validate.isTrue(response.success, "Custom error (" + response.id + "): " + response.error);
            sender.sendMessage("Uploaded here: " + response.url);
            System.out.println("Latest logs uploaded at " + response.url);

        } catch (Exception exception) {
            exception.printStackTrace();
            sender.sendMessage("Could not upload latest logs: " + exception.getMessage() + " (check console for stack strace)");
        }

        return CommandResult.SUCCESS;
    }
}
