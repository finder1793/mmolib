package io.lumine.mythic.lib.api.util.ui;

import io.lumine.mythic.lib.MythicLib;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * If you're going to do something, do it with style.
 * <p></p>
 * This interface allows to easily and consistently style
 * your feedback.
 * <p></p>
 * Formatting Codes:
 * <p><code><b>$b</b></code> Normal Body Text
 * </p><code><b>$e</b></code> Example Value / Recommendation
 * <p><code><b>$i</b></code> User Input
 * </p><code><b>$r</b></code> Operation Result
 * <p><code><b>$s</b></code> Success
 * </p><code><b>$f</b></code> Failure
 *
 * @author Gunging
 */
@SuppressWarnings({"NotNullFieldNotInitialized", "unused", "SpellCheckingInspection"})
public abstract class FriendlyFeedbackPalette {

    @NotNull String bodyFormat;
    @NotNull String bodyFormatConsole;
    /**
     * Most of your message will be colored this way, it is the base text colour (with no highlighting of any kind).
     * <p></p>
     * Preferably a neutral color, not too dark nor too light (consider dark and light consoles).
     */
    @NotNull public String getBodyFormat() { return bodyFormat; }
    /**
     * <b>Body Format</b> but when messages are sent to the console, since it doesnt
     * support HEX color codes (while allowing to send messages in HEX to players!)
     * @see #getBodyFormat()
     */
    @NotNull public String consoleBodyFormat() { return bodyFormatConsole; }

    @NotNull String exampleHighlight;
    @NotNull String exampleHighlightConsole;
    /**
     * If you are going to provide an example/recommended value to the
     * user, highlight it this way with <b>$e</b>.
     * <p></p>
     * Example:
     * <p><code>Log("You should try setting consumeOnUse to <b>$etrue</b>")</code></p>
     */
    @NotNull public String getExampleFormat() { return exampleHighlight; }
    /**
     * <b>Example Format</b> but when messages are sent to the console, since it doesnt
     * support HEX color codes (while allowing to send messages in HEX to players!)
     * @see #getExampleFormat()
     */
    @NotNull public String consoleExampleFormat() { return exampleHighlightConsole; }

    @NotNull String inputHighlight;
    @NotNull String inputHighlightConsole;
    /**
     * Tell the user what they are telling you with <b>$u</b>.
     * <p></p>
     * It may sound trivial, but a huge confusion may arise when you
     * tell a user you're expecting a numeric value and they swear they
     * are sending a number, but they are writing this number in the
     * <i>wrong</i> place.
     * <p></p>
     * Example:
     * <p><code>Log("Expected a number instead of <b>$u" + userInput</b>)</code></p>
     */
    @NotNull public String getInputFormat() { return inputHighlight; }
    /**
     * <b>Input Format</b> but when messages are sent to the console, since it doesnt
     * support HEX color codes (while allowing to send messages in HEX to players!)
     * @see #getInputFormat()
     */
    @NotNull public String consoleInputFormat() { return inputHighlightConsole; }

    @NotNull String resultHighlight;
    @NotNull String resultHighlightConsole;
    /**
     * Tell the user the result of your operation with <b>$r</b>.
     * <p></p>
     * Usually after processing their input or something.
     * <p></p>
     * Example:
     * <p><code>Log("Searched ores near you and found <b>$r" + oresFound</b>)</code></p>
     */
    @NotNull public String getResultFormat() { return resultHighlight; }
    /**
     * <b>Result Format</b> but when messages are sent to the console, since it doesnt
     * support HEX color codes (while allowing to send messages in HEX to players!)
     * @see #getResultFormat()
     */
    @NotNull public String consoleResultFormat() { return resultHighlightConsole; }

    @NotNull String successHighlight;
    @NotNull String successHighlightConsole;
    /**
     * Sometimes one must be clearer when specifying if an operation
     * completed sucessfully. In such cases, highlight a word
     * indicating success with <b>$s</b>.
     * <p></p>
     * Example:
     * <p><code>Log("That animal <b>$smatched</b> $bthe name specified.")</code></p>
     */
    @NotNull public String getSuccessFormat() { return successHighlight; }
    /**
     * <b>Success Format</b> but when messages are sent to the console, since it doesnt
     * support HEX color codes (while allowing to send messages in HEX to players!)
     * @see #getSuccessFormat()
     */
    @NotNull public String consoleSuccessFormat() { return successHighlightConsole; }

    @NotNull String failureHighlight;
    @NotNull String failureHighlightConsole;
    /**
     * Sometimes one must be clearer when specifying if an operation
     * failed or was cancelled. In such cases, highlight a word
     * indicating failure with <b>$f</b>.
     * <p></p>
     * Example:
     * <p><code>Log("That animal <b>$fdid not match</b> $bthe name specified.")</code></p>
     */
    @NotNull public String getFailureFormat() { return failureHighlight; }
    /**
     * <b>Failure Format</b> but when messages are sent to the console, since it doesnt
     * support HEX color codes (while allowing to send messages in HEX to players!)
     * @see #getFailureFormat()
     */
    @NotNull public String consoleFailureFormat() { return failureHighlightConsole; }

    @NotNull String brandPrefix;
    @NotNull String brandPrefixConsole;
    /**
     * Probably the prefix of your plugin, or some brand-like trademark.
     * <p></p>
     * The symbol key <b>#s</b> stands for some 'subdivision' which will
     * be inserted if not null.
     * <p></p>
     * Example: (<code>&3[&eGooP#s&3] </code>)<p>
     * <i>Note the space in this prefix example.</i>
     */
    @NotNull String getPrefix(@Nullable String subdivision) {

        if (subdivision != null) {

            // Append subdibivision
            return brandPrefix.replace("#s", " " + getSubdivisionFormat() + subdivision);

        } else {

            // Remove #s
            return brandPrefix.replace("#s", "");
        }
    }
    /**
     * <b>Prefix</b> but when messages are sent to the console, since it doesnt
     * support HEX color codes (while allowing to send messages in HEX to players!)
     * @see #getPrefix(String)
     */
    @NotNull String consolePrefix(@Nullable String subdivision) {

        if (subdivision != null) {

            // Append subdibivision
            return brandPrefixConsole.replace("#s", " " + consoleSubdivisionFormat() + subdivision);

        } else {

            // Remove #s
            return brandPrefixConsole.replace("#s", "");
        }
    }

    @NotNull String subdivisionFormat;
    @NotNull String subdivisionFormatConsole;
    /**
     * In your prefix, you may specify a word or something for the sub-product
     * or whatever (which is fancy), but is not always displayed. If it is,
     * it will be preceded by this color code.
     */
    @NotNull String getSubdivisionFormat() { return subdivisionFormat; }
    /**
     * <b>Subdivision Format</b> but when messages are sent to the console, since it doesnt
     * support HEX color codes (while allowing to send messages in HEX to players!)
     * @see #getSubdivisionFormat()
     */
    @NotNull public String consoleSubdivisionFormat() { return subdivisionFormatConsole; }

    /**
     *   Parses a message intended to be read in-game.
     *   Basically supporting HEX codes in 1.16+
     *   <p></p>
     *   Will delegate to {@link #parseForConsole(String)}
     *   in previous minecraft versions because it is assumed
     *   that the console colors have no HEX
     */
    @NotNull public String parseForPlayer(@NotNull String messaeg) {

        // If below 1.16, parse as console (which is guaranteed to have no HEX colors)
        if (!MythicLib.plugin.getVersion().isStrictlyHigher(1, 16)) { return parseForConsole(messaeg); }

        // Ay
        return messaeg
                .replace("$b", getBodyFormat())
                .replace("$e", getExampleFormat())
                .replace("$i", getInputFormat())
                .replace("$s", getSuccessFormat())
                .replace("$f", getFailureFormat())
                .replace("$r", getResultFormat());
    }

    /**
     * Parses a message intended to be read through the console.
     * Consoles do'nt support many colors, in fact, they support
     * the same colors supported until MC 1.15
     * <p></p>
     * Will also use this method if parsing a message in a version
     * of minecraft less than 1.16
     */
    @NotNull public String parseForConsole(@NotNull String messaeg) {

        // Ay
        return messaeg
                .replace("$b", consoleBodyFormat())
                .replace("$e", consoleExampleFormat())
                .replace("$i", consoleInputFormat())
                .replace("$s", consoleSuccessFormat())
                .replace("$f", consoleFailureFormat())
                .replace("$r", consoleResultFormat());
    }
}
