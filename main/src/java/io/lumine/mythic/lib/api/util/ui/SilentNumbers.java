package io.lumine.mythic.lib.api.util.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A few methods to attempt to parse Strings into Numbers. Also a few methods to parse Math into Strings.
 * <p></p>
 * These methods are designed to fail <b>silently</b>, use that to your advantage.
 * Basically, they will return <code>null</code> instead of throwing exceptions,
 * admitting even input that makes no sense without generating errors.
 *
 * @see io.lumine.utils.numbers.Numbers
 * @author Gunging
 */
@SuppressWarnings("unused")
public class SilentNumbers {

    //region Purest Math Parsing
    /*
     *   Try-Parsers
     */
    /**
     * Is this either <code>true</code> or <code>false</code>? (Ignores caps)
     *
     * @param b Any text piece that may not even exist.
     * @return <b><code>true</code></b> if and only if the string parses into a boolean value
     */
    public static boolean BooleanTryParse(@Nullable String b) {
        // Nothing straight up does not parse
        if (b == null) { return false; }

        // Attempt
        return b.equalsIgnoreCase("true") || b.equalsIgnoreCase("false");
    }
    /**
     * Can you parse a double value from this?
     *
     * @param d Any text piece that may not even exist.
     * @return <b><code>true</code></b> if and only if the string parses into a number
     */
    public static boolean DoubleTryParse(@Nullable String d) { return (DoubleParse(d) != null); }
    /**
     * Can you parse an int value from this?
     * <p>This one is not as sensitive as {@link Integer#parseInt(String)},
     * <b>this will actually read an integer</b> value from a number with trailing
     * decimal zeroes like <code>3.0000</code></p>
     *
     * @param i Any text piece that may not even exist.
     * @return <b><code>true</code></b> if and only if the string parses into a number.
     */
    public static boolean IntTryParse(@Nullable String i) { return (IntegerParse(i) != null);}

    /*
     *   Straight up parsing
     */
    /**
     * Reads a string saying <code>true</code> or <code>false</code> (ignoring caps).
     * @return <code>null</code> in case of any error, or your boolean value.
     */
    @Nullable public static Boolean BooleanParse(@Nullable String b) {

        // Straight up no
        if (b == null) { return null;}

        // Well does it work?
        if (b.equalsIgnoreCase("true")) { return true;
        } else if (b.equalsIgnoreCase("false")) { return false; }

        // Nope
        return null;
    }
    /**
     * Straight up {@link Double#parseDouble(String)}.
     * <p></p>
     * However, instead of throwing an exception, this method will return <code>null</code>.
     * @return <code>null</code> in case of any error, or your double value.
     */
    @Nullable public static Double DoubleParse(@Nullable String d) {

        // Straight up no
        if (d == null) { return null; }

        // Attempt
        try { return Double.parseDouble(d);

        // Nope
        } catch (NumberFormatException e) { return null; }
    }
    /**
     * Parses an int from a string even if it contains a decimal point (But it must be followed only by zeros)
     * @return <code>null</code> in case of any error, or your integer value.
     */
    @Nullable public static Integer IntegerParse(@Nullable String i) {
        // No more null
        if (i == null) { return null; }

        // Well attempt to parse that...
        try { return Integer.parseInt(RemoveDecimalZeros(i));

        // That's an L
        } catch (NumberFormatException ignored) { return null; }
    }
    //endregion

    //region Math Utilities
    /**
     * Rounds a double to any amount of decimal places.
     */
    public static double Round(double number, int decimals) {
        long rounded = Math.round(number * Math.pow(10, decimals));
        return rounded / Math.pow(10, decimals);
    }
    /**
     * Rounds a double into an integer
     */
    public static int Round(double number) { return (int) Math.round(number); }

    /**
     * Returns:
     * <p><b><code>1</code></b> if true
     * </p><b><code>0</code></b> if false
     */
    public static int IntegerParse(boolean value) { if (value) { return 1; } else { return  0; } }
    /**
     * Is this integer equal to <code>0</code>?
     */
    public static boolean BooleanParse(int value) { return value != 0; }
    /**
     * Is this integer equal to <code>0.0</code>?
     */
    public static boolean BooleanParse(double value) { return value != 0D; }
    //endregion

    //region Nice UI Functions
    /**
     * If given some value that ends in <code>.000</code> (any number of zeros),
     * it will remove the decimal point and the zeros.
     * If there is any decimal number, it will remove all zeros after it:
     * <p></p>
     * Examples:
     * <p><b><code>"1.0000"</code></b> will return <b><code>"1"</code></b>
     * </p><b><code>"1.000100"</code></b> will return <b><code>"1.0001"</code></b>
     * <p></p>
     * Intended to parse '8.0' as an integer value "8"; BRUH
     * <p></p>
     * If the value is not a number that ends in <code>.0000...</code>, it will return it unchanged.
     *
     * @author Gunging
     */
    @NotNull public static String RemoveDecimalZeros(@NotNull String source) {

        // Does it have a decimal to begin with?
        if (source.contains(".")) {

            // Get
            String decimals = source.substring(source.lastIndexOf("."));

            // Find last nonzero char
            int lC = -1;

            // Evaluate all zeroes
            for (int c = 1; c < decimals.length(); c++) {

                // Get Char
                char ch = decimals.charAt(c);

                // Is it not a zero
                if (ch != '0') {

                    // AH cancel
                    lC = c;
                }
            }

            // Return thay
            return source.substring(0, source.lastIndexOf(".") + lC + 1);
        }

        // AH cancel
        return source;
    }

    /**
     * When you round a <code>double</code>, and try to use {@link String#valueOf(double)}, it
     * will show up as "<b><code>2.0</code></b>. I think that <code>.0</code> is ugly asf.
     * <p></p>
     * This method will remove such .0
     * <p></p>
     * Also, if the input is something like <code>2.03003</code>, and it rounds to 4 decimals as
     * <code>"2.0300"</code>, this will remove those pesky <code>00</code>s and return just <code>"2.03"</code>
     *
     * @author Gunging
     */
    @NotNull public static String ReadableRounding(double something, int decimals) {

        // Round to decimals ig
        return RemoveDecimalZeros(String.valueOf(Round(something, decimals)));
    }

    /**
     * Say you have a big number of seconds you want to display to the user.
     * Well, this compresses it into minutes or hours depending on how big
     * this number is, attempting not to exceed 3 characters length.
     * <p></p>
     * So basically, <code>72</code> will be returned as <code>"72s"</code>,
     * but once you go into like <code>1800</code>, it will become <code>"30m"</code>
     *
     * @author Gunging
     */
    @NotNull public static String NicestTimeValueFrom(double seconds) {

        // If more than 1 minute
        if (seconds > 60) {

            // Is it greater than 1800?
            if (seconds > 1800) {

                // Dive by 1800
                double div1800 = seconds / 1800.0D;

                // Get the difference from an integer rounding [0-0.99]
                double difference = Math.round(div1800) - div1800;

                // If it was nice (within 9 minutes of half an hour, continue as hours)
                if (difference < 0.34) {

                    // Return as minutes alv
                    return ReadableRounding(Round(seconds / 3600.0D, 1),1) + "h";
                }

                // Difference was kinda sensitive. Will evaluate as minutes I guess

                // BUT first, if it would hit 1000 minutes, force-convert to hours ~ with two decimal places :)
                if (seconds > 60000) {

                    // Return as minutes alv
                    return ReadableRounding(Round(seconds / 3600.0D, 2), 1) + "h";
                }
            }

            // Dive by 30
            double div30 = seconds / 30.0D;

            // Get the difference from an integer rounding [0-0.99]
            double difference = Math.round(div30) - div30;

            // If it was nice (within 9 seconds of half a minute, continue as minutes)
            if (difference < 0.34) {

                // Return as minutes alv
                return ReadableRounding(Round(seconds / 60.0D, 1), 1) + "m";
            }

            // Difference was kinda sensitive. Will use seconds

            // BUT first, if it would hit 1000 seconds, force-convert to seconds ~ with two decimal places :)
            if (seconds > 1000) {

                // Return as minutes alv
                return ReadableRounding(Round(seconds / 60.0D, 2), 1) + "m";
            }
        }

        // Return as seconds alv
        return ReadableRounding(Round(seconds, 1), 1) + "s";
    }

    /**
     * Can this string represent a UUID? If so, return it as an UUID!
     */
    @Nullable public static UUID UUIDParse(@Nullable String anything) {

        // Straight up no
        if (anything == null) { return null; }

        // Correct Format?
        if (anything.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {

            // Return thay
            return UUID.fromString(anything);
        }

        // No
        return null;
    }
    //endregion
}
