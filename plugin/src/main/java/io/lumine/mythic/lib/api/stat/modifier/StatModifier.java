package io.lumine.mythic.lib.api.stat.modifier;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import io.lumine.mythic.lib.player.modifier.PlayerModifier;
import org.apache.commons.lang.Validate;

import java.text.DecimalFormat;

public class StatModifier extends PlayerModifier {
    private final String stat;
    private final double value;
    private final ModifierType type;

    private static final DecimalFormat oneDigit = MythicLib.plugin.getMMOConfig().newDecimalFormat("0.#");

    /**
     * Flat stat modifier (simplest modifier you can think about)
     */
    public StatModifier(String key, String stat, double value) {
        this(key, stat, value, ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    /**
     * Stat modifier given by an external mecanic, like a party buff, item set bonuses,
     * skills or abilities... Anything apart from items and armor.
     */
    public StatModifier(String key, String stat, double value, ModifierType type) {
        this(key, stat, value, type, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    /**
     * Stat modifier given by an item, either a weapon or an armor piece.
     *
     * @param stat   Stat being modified
     * @param key    Player modifier key
     * @param value  Value of stat modifier
     * @param type   Is the modifier flat or multiplicative
     * @param slot   Slot of the item granting the stat modifier
     * @param source Type of the item granting the stat modifier
     */
    public StatModifier(String key, String stat, double value, ModifierType type, EquipmentSlot slot, ModifierSource source) {
        super(key, slot, source);

        this.stat = stat;
        this.value = value;
        this.type = type;
    }

    /**
     * Used to parse a StatModifier from a string in a configuration section.
     * Always returns a modifier with source OTHER. Can be used by MythicCore
     * to handle party buffs, or MMOItems for item set bonuses. Throws IAE
     *
     * @param str The string to be parsed
     */
    public StatModifier(String key, String stat, String str) {
        super(key, EquipmentSlot.OTHER, ModifierSource.OTHER);

        Validate.notNull(str, "String cannot be null");
        Validate.notEmpty(str, "String cannot be empty");

        type = str.toCharArray()[str.length() - 1] == '%' ? ModifierType.RELATIVE : ModifierType.FLAT;
        value = Double.parseDouble(type == ModifierType.RELATIVE ? str.substring(0, str.length() - 1) : str);
        this.stat = stat;
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
        return new StatModifier(getKey(), stat, value * coef, type, getSlot(), getSource());
    }

    public ModifierType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    @Override
    public void register(MMOPlayerData playerData) {

    }

    @Override
    public void unregister(MMOPlayerData playerData) {

    }

    @Override
    public String toString() {
        return oneDigit.format(value) + (type == ModifierType.RELATIVE ? "%" : "");
    }
}

