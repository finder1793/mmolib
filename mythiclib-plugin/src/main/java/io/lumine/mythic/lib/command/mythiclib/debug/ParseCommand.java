package io.lumine.mythic.lib.command.mythiclib.debug;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.command.api.CommandTreeNode;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class ParseCommand extends CommandTreeNode {
    public ParseCommand(CommandTreeNode parent) {
        super(parent, "parse");
    }

    @Override
    public CommandResult execute(CommandSender sender, String[] args) {
        final String format = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        //sender.sendMessage("Evaluating '" + format + "'");
        sender.sendMessage(String.valueOf(MythicLib.plugin.getFormulaParser().evaluate(format)));
        return CommandResult.SUCCESS;
    }
}
