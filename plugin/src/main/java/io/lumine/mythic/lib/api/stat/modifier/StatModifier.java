package io.lumine.mythic.lib.api.stat.modifier;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.player.PlayerModifier;
import org.apache.commons.lang.Validate;

import java.text.DecimalFormat;

public class StatModifier extends PlayerModifier {
    private final double d;
    private final ModifierType type;

    private static final DecimalFormat oneDigit = MythicLib.plugin.getMMOConfig().newDecimalFormat("0.#");

    /**
     * Flat stat modifier (simplest modifier you can think about)
     */
    public StatModifier(double d) {
        this(d, ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    /**
     * Stat modifier given by an external mecanic, like a party buff, item set bonuses,
     * skills or abilities... Anything apart from items and armor.
     */
    public StatModifier(double d, ModifierType type) {
        this(d, type, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    /**
     * Stat modifier given by an item, either a weapon or an armor piece.
     *
     * @param slot   Slot of the item granting the stat modifier
     * @param source Type of the item granting the stat modifier
     */
    public StatModifier(double d, ModifierType type, EquipmentSlot slot, ModifierSource source) {
        super(slot, source);

        this.d = d;
        this.type = type;
    }

    /**
     * Clones a stat modifier
     */
    public StatModifier(StatModifier mod) {
        this(mod.d, mod.type, mod.getSlot(), mod.getSource());
    }

    /**
     * Used to parse a StatModifier from a string in a configuration section.
     * Always returns a modifier with source OTHER. Can be used by MythicCore
     * to handle party buffs, or MMOItems for item set bonuses. Throws IAE
     *
     * @param str The string to be parsed
     */
    public StatModifier(String str) {
        super(EquipmentSlot.OTHER, ModifierSource.OTHER);

        Validate.notNull(str, "String cannot be null");
        Validate.notEmpty(str, "String cannot be empty");

        type = str.toCharArray()[str.length() - 1] == '%' ? ModifierType.RELATIVE : ModifierType.FLAT;
        d = Double.parseDouble(type == ModifierType.RELATIVE ? str.substring(0, str.length() - 1) : str);
    }

    /**
     * Used to multiply some existing stat modifier by a constant, usually an
     * integer, for instance when MMOCore party modifiers scale with the
     * number of the party member count
     *
     * @param coef The multiplicative constant
     * @return A new instance of StatModifier with modified value
     */
    public StatModifier multiply(double coef) {
        return new StatModifier(d * coef, type, getSlot(), getSource());
    }

    public ModifierType getType() {
        return type;
    }

    public double getValue() {
        return d;
    }

    @Override
    public String toString() {
        return oneDigit.format(d) + (type == ModifierType.RELATIVE ? "%" : "");
    }
}

