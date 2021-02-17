package io.lumine.mythic.lib.api.util.ui;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * 'Whats the difference between a villain and a super villain?'
 */
@SuppressWarnings("unused")
public class FriendlyFeedbackPalette_MythicLib extends FriendlyFeedbackPalette {

    // Most text of the message
    @NotNull String bodyFormat = "§x§8§7§8§5§7§2";
    @NotNull String bodyFormatConsole = ChatColor.GRAY.toString();

    // Examples highlight
    @NotNull String exampleHighlight = "§x§f§f§d§8§3§b";
    @NotNull String exampleHighlightConsole = ChatColor.GOLD.toString();

    // User Input Highlight
    @NotNull String inputHighlight = "§x§f§c§f§f§7§8";
    @NotNull String inputHighlightConsole = ChatColor.YELLOW.toString();

    // Operation Result Highlight
    @NotNull String resultHighlight = "§x§d§9§f§f§6§9";
    @NotNull String resultHighlightConsole = ChatColor.AQUA.toString();

    // Success
    @NotNull String successHighlight = "§x§0§0§f§f§0§0";
    @NotNull String successHighlightConsole = ChatColor.GREEN.toString();

    // Failure
    @NotNull String failureHighlight = "§x§f§f§4§7§4§7";
    @NotNull String failureHighlightConsole = ChatColor.RED.toString();

    // Prefix
    @NotNull String brandPrefix = "§x§5§7§3§9§2§5[§f§f§a§e§0§0MythicLib§x§5§7§3§9§2§5]";
    @NotNull String brandPrefixConsole = "§8[§6MythicLib#s§8]";

    // Subdivision Color Code
    @NotNull String subdivisionFormat = "§x§e§b§8§2§3§1§o";
    @NotNull String subdivisionFormatConsole = "§c§o";
}
