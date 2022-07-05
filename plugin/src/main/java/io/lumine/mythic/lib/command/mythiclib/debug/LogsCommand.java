package io.lumine.mythic.lib.command.mythiclib.debug;

import gs.mclo.java.APIResponse;
import gs.mclo.java.MclogsAPI;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.command.api.CommandTreeNode;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            File log = new File(MythicLib.plugin.getDataFolder(), "..\\..\\logs\\latest.log");
            Scanner scanner = new Scanner(log);
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine())
                builder.append(scanner.nextLine()).append("\n");
            scanner.close();

            final String logsDir = MythicLib.plugin.getDataFolder().getParentFile().getAbsoluteFile().getParentFile().getAbsoluteFile() + "/logs/";
            final String file = "latest.log";

            APIResponse response = MclogsAPI.share(logsDir, file);
            Validate.isTrue(response.success, "Custom error (" + response.id + "): " + response.error);
            sender.sendMessage("Uploaded here: " + response.url);

        } catch (Exception exception) {
            exception.printStackTrace();
            sender.sendMessage("Could not upload latest logs: " + exception.getMessage() + " (check console for stack strace)");
        }

        return CommandResult.SUCCESS;
    }
}
