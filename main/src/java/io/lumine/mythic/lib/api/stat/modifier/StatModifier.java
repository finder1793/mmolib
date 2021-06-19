package io.lumine.mythic.lib.api.stat.modifier;

import io.lumine.mythic.lib.MythicLib;
import org.apache.commons.lang.Validate;

public class StatModifier {
    private final double d;
    private final ModifierType type;
    private final ModifierSource source;

    public StatModifier(double d) {
        this(d, ModifierType.FLAT, ModifierSource.OTHER);
    }

    /**
     * @deprecated Use the constructor with a ModifierSource instead.
     */
    @Deprecated
    public StatModifier(double d, ModifierType type) {
        this(d, type, ModifierSource.OTHER);
    }

    public StatModifier(double d, ModifierType type, ModifierSource source) {
        this.d = d;
        this.type = type;
        this.source = source;
    }

    public StatModifier(StatModifier mod) {
        this(mod.d, mod.type, mod.source);
    }

    /**
     * Used to parse a StatModifier from a string in a configuration section.
     * Always returns a modifier with source OTHER. Can be used by MythicCore
     * to handle party buffs, or MMOItems for item set bonuses. Throws IAE
     *
     * @param str The string to be parsed
     */
    public StatModifier(String str) {
        Validate.notNull(str, "String cannot be null");
        Validate.notEmpty(str, "String cannot be empty");

        type = str.toCharArray()[str.length() - 1] == '%' ? ModifierType.RELATIVE : ModifierType.FLAT;
        d = Double.parseDouble(type == ModifierType.RELATIVE ? str.substring(0, str.length() - 1) : str);
        source = ModifierSource.OTHER;
    }

    /**
     * Used to multiply some existing stat modifier by a constant, usually an
     * integer, for instance when modifiers scale with the number of players (in
     * a party, etc)
     *
     * @param coef
     *            The multiplicative constant
     * @return A new instance of StatModifier with modified value
     */
    public StatModifier multiply(double coef) {
        return new StatModifier(d * coef, type, source);
    }

    public ModifierType getType() {
        return type;
    }

    public ModifierSource getSource() {
        return source;
    }

    public double getValue() {
        return d;
    }

    @Override
    public String toString() {
        return MythicLib.plugin.getMMOConfig().decimal.format(d) + (type == ModifierType.RELATIVE ? "%" : "");
    }
}

