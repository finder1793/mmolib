package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager;
import io.lumine.mythic.lib.element.Element;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class DamageMetadata implements Cloneable {
    private final Set<DamagePacket> packets = new HashSet<>();

    /**
     * Used to register an attack with NO initial packet!
     */
    public DamageMetadata() {
    }

    /**
     * Used to register a attack
     *
     * @param damage The attack damage
     * @param types  The attack damage types
     */
    public DamageMetadata(double damage, DamageType... types) {
        packets.add(new DamagePacket(damage, types));
    }

    public double getDamage() {
        double d = 0;

        for (DamagePacket packet : packets) {

            MythicCraftingManager.log("\u00a78DAMAGE\u00a77 Adding up damage: \u00a7a" + packet.toString());
            d += packet.getFinalValue();
        }

        MythicCraftingManager.log("\u00a78DAMAGE\u00a77 Total: \u00a7a" + d);

        return d;
    }

    public double getDamage(DamageType type) {
        double d = 0;

        for (DamagePacket packet : packets)
            if (packet.hasType(type))
                d += packet.getFinalValue();

        return d;
    }

    public Set<DamagePacket> getPackets() {
        return packets;
    }

    /**
     * @return Set containing all damage types found
     *         in all the different damage packets.
     */
    public Set<DamageType> collectTypes() {
        Set<DamageType> collected = new HashSet<>();

        for (DamagePacket packet : packets)
            for (DamageType type : packet.types)
                collected.add(type);

        return collected;
    }

    /**
     * @return Iterates through all registered damage packets and
     *         see if any has this damage type.
     */
    public boolean hasType(DamageType type) {

        for (DamagePacket packet : packets)
            if (packet.hasType(type))
                return true;

        return false;
    }

    /**
     * Registers a new damage packet.
     *
     * @param value Damage dealt by another source, this could be an on-hit
     *              skill increasing the damage of the current attack.
     * @param types The damage types of the packet being registered
     * @return The same modified damage metadata
     */
    public DamageMetadata add(double value, DamageType... types) {
        packets.add(new DamagePacket(value, types));
        return this;
    }

    /**
     * Registers a new elemental damage packet.
     *
     * @param value   Damage dealt by another source, this could be an on-hit
     *                skill increasing the damage of the current attack.
     * @param element The element
     * @param types   The damage types of the packet being registered
     * @return The same modified damage metadata
     */
    public DamageMetadata add(double value, Element element, DamageType... types) {
        packets.add(new ElementalDamagePacket(value, element, types));
        return this;
    }

    /**
     * Register a multiplicative damage modifier in all damage packets.
     * <p>
     * This is used for critical strikes which modifier should
     * NOT stack up with damage boosting statistics.
     *
     * @param coef Multiplicative coefficient. 1.5 will
     *             increase final damage by 50%
     * @return The same damage metadata
     */
    public DamageMetadata multiplicativeModifier(double coef) {
        for (DamagePacket packet : packets)
            packet.value *= coef;
        return this;
    }

    /**
     * Registers a multiplicative damage modifier
     * which applies to any damage packet
     *
     * @param multiplier From 0 to infinity, 1 increases damage by 100%.
     *                   This can be negative as well
     * @return The same damage metadata
     */
    public DamageMetadata additiveModifier(double multiplier) {
        for (DamagePacket packet : packets)
            packet.additiveModifiers += multiplier;
        return this;
    }

    /**
     * Register a multiplicative damage modifier for a specific
     * damage type.
     * <p>
     * This is not being used in MMOCore nor MMOItems
     *
     * @param coef      Multiplicative coefficient. 1.5 will
     *                  increase final damage by 50%
     * @param concerned Concerned damage type
     * @return The same damage metadata
     */
    public DamageMetadata multiplicativeModifier(double coef, @NotNull DamageType concerned) {
        for (DamagePacket packet : packets)
            if (packet.hasType(concerned))
                packet.value *= coef;
        return this;
    }

    /**
     * Registers a multiplicative damage modifier which only
     * applies to a specific damage type
     *
     * @param multiplier From 0 to infinity, 1 increases damage by 100%.
     *                   This can be negative as well
     * @param concerned  Specific damage type
     * @return The same damage metadata
     */
    public DamageMetadata additiveModifier(double multiplier, @NotNull DamageType concerned) {
        for (DamagePacket packet : packets)
            if (packet.hasType(concerned))
                packet.additiveModifiers += multiplier;
        return this;
    }

    @Override
    public DamageMetadata clone() {
        DamageMetadata clone = new DamageMetadata();

        for (DamagePacket packet : packets)
            clone.packets.add(packet);

        return clone;
    }

    /**
     * Some damage value weighted by a specific set of damage types. This helps
     * divide any attack into multiple parts that can be manipulated independently.
     * <p>
     * For instance, a melee sword attack would add one physical-weapon damage packet.
     * Then, casting an on-hit ability like Starfall would add an extra magic-skill
     * damage packet, independently of the packet that is already there. If we were
     * to then apply the 'Melee Damage' stat, it would only apply to the first packet.
     * <p>
     * Damage packets are completed hidden from developpers. Just like nodes
     * for the HashMap implementation.
     *
     * @author jules
     */
    class DamagePacket {
        private final DamageType[] types;
        private double value, additiveModifiers;

        public DamagePacket(double value, DamageType... types) {
            this.value = value;
            this.types = types;
        }

        /**
         * @return Final value of the damage packet taking into account
         *         all the damage modifiers that have been registered
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
            StringBuilder sb = new StringBuilder();
            for (DamageType d : types) { if (sb.length() != 0) { sb.append('/'); } sb.append(d.toString()); }
            sb.append(" x").append(value);

            // Yes
            return sb.toString();
        }
    }

    class ElementalDamagePacket extends DamagePacket {
        private final Element element;

        public ElementalDamagePacket(double value, Element element, DamageType... types) {
            super(value, types);

            this.element = element;
        }
    }
}
