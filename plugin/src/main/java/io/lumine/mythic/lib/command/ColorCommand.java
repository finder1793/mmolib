package io.lumine.mythic.lib.command;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.comp.adventure.AdventureParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * mythiclib
 * 01/12/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class ColorCommand implements CommandExecutor {

    private final AdventureParser parser;

    public ColorCommand(MythicLib plugin, AdventureParser parser) {
        this.parser = parser;
        plugin.getCommand("color").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /color <text>");
            return true;
        }
        String text = String.join(" ", args);
        parser.parseAsync(text).thenAccept(sender::sendMessage);
        return true;
    }
}
