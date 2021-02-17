package io.lumine.mythic.lib.api.util.ui;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * In our case, us developers, Java throws {@link Exception}s at us when we fuck something up.
 * <p></p>
 * Normal users don't need to be blasted with such technical messages, but they also
 * make mistakes. This interface is meant to provide a better user experience by telling
 * the user why their input has failed with easier implementation in our side (not having
 * to check the input before trying to use it elsewhere).
 * <p></p>
 * This is designed with <b>commands</b> in mind, where the only user input is ultimately
 * {@link String}s, and the messages users can receive are console lines or chat messages.
 *
 * @author Gunging
 */
@SuppressWarnings("unused")
public class FriendlyFeedbackProvider {

    //region Main
    @NotNull FriendlyFeedbackPalette ffPalette;

    /**
     * The palette used by this feedback provider.
     * <p></p>
     * It has the color stuff, and the prefix most notably.
     */
    public FriendlyFeedbackPalette getPalette() { return ffPalette; }

    /**
     * To initialize a feedback provider you just need a palette.
     */
    public FriendlyFeedbackProvider(@NotNull FriendlyFeedbackPalette palette) {
        ffPalette = palette;
    }
    //endregion

    //region Collecting Messages
    @NotNull HashMap<FriendlyFeedbackCategory, ArrayList<FriendlyFeedbackMessage>> feedback = new HashMap<>();

    /**
     * Get the feedback of this category that has been registered.
     * <p></p>
     * Will return an emtpy array if no messages have been issued concerning this topic.
     */
    @NotNull public ArrayList<FriendlyFeedbackMessage> getFeedbackOf(@NotNull FriendlyFeedbackCategory category) {

        // Make sure it is registered
        return feedback.computeIfAbsent(category, k -> new ArrayList<>());
    }

    /**
     * Include a message to be sent later under this category.
     * <b>This does not actually send a message</b>
     * <p></p>
     * Fails silently if the message is <code>null</code> or empty.
     * <p></p>
     * The first <code>$b</code> is included. Check out the codes
     * accepted in the description of {@link FriendlyFeedbackPalette}
     * @param replaces The (ordered) list of string variables to be replaced.
     *                 <p></p>
     *                 Suppose your <code>message</code> is
     *                 <b><code>"Your input $i{0}$b is not a number!"</code></b>
     *                 <p></p>
     *                 This means that the first element of the array will be
     *                 inserted in the place of that <code>{0}</code>.
     */
    public void Log(@NotNull FriendlyFeedbackCategory category, @Nullable String message, String... replaces) {

        // Cancel if null
        if (message == null) { return; }
        if (message.isEmpty()) { return; }

        // Bake message
        for (int i = 0; i < replaces.length; i++) { message = message.replace("{" + i + "}", replaces[i]); }

        // Build
        FriendlyFeedbackMessage msg = prefixSample.clone();
        msg.setMessage("$b" + message);

        // Add prefix and add
        getFeedbackOf(category).add(msg);
    }

    /**
     * Include a message to be sent later under this category.
     * <b>This does not actually send a message</b>
     * <p></p>
     * Fails silently if the message is <code>null</code> or empty.
     * <p></p>
     * The first <code>$b</code> is included. Check out the codes
     * accepted in the description of {@link FriendlyFeedbackPalette}
     * <p></p>
     * Fails silently if the provided FFP is <code>null</code>.
     * @param replaces The (ordered) list of string variables to be replaced.
     *                 <p></p>
     *                 Suppose your <code>message</code> is
     *                 <b><code>"Your input $i{0}$b is not a number!"</code></b>
     *                 <p></p>
     *                 This means that the first element of the array will be
     *                 inserted in the place of that <code>{0}</code>.
     */
    public static void Log(@Nullable FriendlyFeedbackProvider ffp, @NotNull FriendlyFeedbackCategory category, @Nullable String message, String... replaces) { if (ffp != null) { ffp.Log(category, message, replaces); } }

    @NotNull FriendlyFeedbackMessage prefixSample = new FriendlyFeedbackMessage("");
    /**
     * Call this method to make incoming messages acquire a prefix of your choosing.
     * <p></p>
     * The prefix is added as soon as the message is registered, so you may change
     * (or remove) the prefix afterwards for new messages without messing with old
     * ones.
     * @param usePrefix Whether to actually use prefix
     * @param subdivision A subdivision to add to the prefix
     */
    public void ActivatePrefix(boolean usePrefix, @Nullable String subdivision) {

        // If used
        prefixSample.togglePrefix(usePrefix);
        prefixSample.setSubdivision(subdivision);
    }
    //endregion

    //region Sending Messages
    /**
     * Sends all stored messages to the console.
     */
    public void SendAllTo(@NotNull Player player, @NotNull ConsoleCommandSender console) {

        // For each category
        for (FriendlyFeedbackCategory cat : feedback.keySet()) {

            // Log all
            SendTo(cat, player);
            SendTo(cat, console);
        }
    }
    /**
     * Sends all stored messages of this category to both a console and a player.
     */
    public void SendTo(@NotNull FriendlyFeedbackCategory category, @NotNull Player player, @NotNull ConsoleCommandSender console) {

        // Send to both I guess
        SendTo(category, player);
        SendTo(category, console);
    }

    /**
     * Sends all stored messages to the console.
     */
    public void SendAllTo(@NotNull Player player) {

        // For each category
        for (FriendlyFeedbackCategory cat : feedback.keySet()) {

            // Log all
            SendTo(cat, player);
        }
    }
    /**
     * Sends all stored messages of this category to a player.
     */
    public void SendTo(@NotNull FriendlyFeedbackCategory category, @NotNull Player player) {

        // Get List and foreach
        for (FriendlyFeedbackMessage msg : getFeedbackOf(category)) {

            // Send to player
            player.sendMessage(MythicLib.plugin.parseColors(msg.forPlayer(getPalette())));
        }
    }

    /**
     * Sends all stored messages to the console.
     */
    public void SendAllTo(@NotNull ConsoleCommandSender console) {

        // For each category
        for (FriendlyFeedbackCategory cat : feedback.keySet()) {

            // Log all
            SendTo(cat, console);
        }
    }
    /**
     * Sends all stored messages of this category to the console.
     */
    public void SendTo(@NotNull FriendlyFeedbackCategory category, @NotNull ConsoleCommandSender console) {

        // Get List and foreach
        for (FriendlyFeedbackMessage msg : getFeedbackOf(category)) {

            // Send to console
            console.sendMessage(MythicLib.plugin.parseColors(msg.forConsole(getPalette())));
        }
    }
    //endregion
}
