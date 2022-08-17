package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.MythicLib;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ConfigManager {
    public final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();

    public DecimalFormat decimal, decimals;
    public boolean playerAbilityDamage;
    public String defenseFormula;

    public void reload() {
        playerAbilityDamage = MythicLib.plugin.getConfig().getBoolean("player-ability-damage");

        // Decimal separator
        formatSymbols.setDecimalSeparator(getFirstChar(MythicLib.plugin.getConfig().getString("number-format.decimal-separator")));
        decimal = newDecimalFormat("0.#");
        decimals = newDecimalFormat("0.##");
        defenseFormula = MythicLib.plugin.getConfig().getString("defense-application", "#damage# * (1 - (#defense# / (#defense# + 100)))");
    }

    /**
     * MMOCore and MMOItems mostly cache the return value of that method
     * in static fields for easy access, therefore a server restart is
     * required when editing the decimal-separator option in the ML config
     *
     * @param pattern Something like "0.#"
     * @return New decimal format with the decimal separator given in the MythicLib
     *         main plugin config.
     */
    public DecimalFormat newDecimalFormat(String pattern) {
        return new DecimalFormat(pattern, formatSymbols);
    }

    private char getFirstChar(String str) {
        return str == null || str.isEmpty() ? '.' : str.charAt(0);
    }
}
