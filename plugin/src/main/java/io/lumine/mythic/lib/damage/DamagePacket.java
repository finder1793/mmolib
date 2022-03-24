package io.lumine.mythic.lib.damage;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Some damage value weighted by a specific set of damage types. This helps
 * divide any attack into multiple parts that can be manipulated independently.
 * <p>
 * For instance, a melee sword attack would add one physical-weapon damage packet.
 * Then, casting an on-hit ability like Starfall would add an extra magic-skill
 * damage packet, independently of the packet that is already there. If we were
 * to then apply the 'Melee Damage' stat, it would only apply to the first packet.
 * <p>
 * Since 1.3.1 it is now possible to create implementations of the DamagePacket
 * class which can be used by other plugins to
 *
 * @author jules
 */
public class DamagePacket implements Cloneable {
    @NotNull
    private DamageType[] types;
    private double value;
    private double additiveModifiers;

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public DamagePacket(double value, @NotNull DamageType... types) {
        this.value = value;
        this.types = types;
    }

    /**
     * @return Damage types by which this damage packet scales
     */
    @NotNull
    public DamageType[] getTypes() {
        return types;
    }

    public void setTypes(@NotNull DamageType[] types) {
        this.types = Objects.requireNonNull(types, "Damage type array cannot be null");
    }

    /**
     * Register a multiplicative damage modifier.
     * <p>
     * This is used for critical strikes which modifier should
     * NOT stack up with damage boosting statistics.
     *
     * @param coefficient Multiplicative coefficient. 1.5 will
     *                    increase final damage by 50%
     */
    public void multiplicativeModifier(double coefficient) {
        this.value *= coefficient;
    }

    public void additiveModifier(double multiplier) {
        this.additiveModifiers += multiplier;
    }

    /**
     * @return Final value of the damage packet taking into account
     * all the damage modifiers that have been registered
     */
    public double getFinalValue() {

        // Make sure the returned value is positive
        return value * Math.max(0, 1 + additiveModifiers);
    }

    /**
     * @return Checks if the current packet has that damage type
     */
    public boolean hasType(DamageType type) {

        for (DamageType checked : this.types)
            if (checked == type)
                return true;

        return false;
    }

    @Override
    public String toString() {
        StringBuilder damageTypes = new StringBuilder();

        // Append value and modifier
        damageTypes.append("\u00a7e").append("(").append(value).append("*").append(additiveModifiers).append(")").append("x");

        // Append Scaling
        boolean damageAppended = false;
        for (DamageType type : types) {
            if (damageAppended) {
                damageTypes.append("\u00a73/");
            }
            damageAppended = true;

            // Color
            switch (type) {
                case WEAPON:
                    damageTypes.append("\u00a77");
                    break;
                case PHYSICAL:
                    damageTypes.append("\u00a78");
                    break;
                case PROJECTILE:
                    damageTypes.append("\u00a7a");
                    break;
                case MAGIC:
                    damageTypes.append("\u00a79");
                    break;
                case SKILL:
                    damageTypes.append("\u00a7f");
                    break;
                default:
                    damageTypes.append("\u00a7c");
                    break;
            }

            // Damage Type
            damageTypes.append(type);
        }

        // Yeah
        return damageTypes.toString();
    }

    @Override
    public DamagePacket clone() {
        DamagePacket clone = new DamagePacket(value, types);
        clone.additiveModifiers = additiveModifiers;
        return clone;
    }
}