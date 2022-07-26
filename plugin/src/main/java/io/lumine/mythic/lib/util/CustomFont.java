package io.lumine.mythic.lib.util;

import org.bukkit.configuration.ConfigurationSection;

public class CustomFont {
    private final char[] characters = new char[11];

    /**
     * Index of the decimal separator in the array
     */
    private final int SEPARATOR_INDEX = 10;

    public CustomFont(ConfigurationSection config) {
        for (int i = 0; i < 10; i++)
            characters[i] = config.getString(String.valueOf(i), "?").charAt(0);
        characters[SEPARATOR_INDEX] = config.getString("dot", "?").charAt(0);
    }

    public String format(String raw) {
        final StringBuilder builder = new StringBuilder();
        for (char c : raw.toCharArray()) {
            final int index = c - '0';
            builder.append(characters[index < 0 || index > 9 ? SEPARATOR_INDEX : index]);
        }

        return builder.toString();
    }
}
