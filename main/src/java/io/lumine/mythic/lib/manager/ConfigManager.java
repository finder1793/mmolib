package io.lumine.mythic.lib.manager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ConfigManager {
    public final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
    public final DecimalFormat decimal = new DecimalFormat("0.#", formatSymbols), decimals = new DecimalFormat("0.##", formatSymbols);

    public void reload() {
        formatSymbols.setDecimalSeparator(getFirstChar(MMOLib.plugin.getConfig().getString("number-format.decimal-separator")));
    }

    public DecimalFormat newFormat(String pattern) {
        return new DecimalFormat(pattern, formatSymbols);
    }

    private char getFirstChar(String str) {
        return str == null || str.isEmpty() ? ',' : str.charAt(0);
    }
}
