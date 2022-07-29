package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.element.Element;
import io.lumine.mythic.lib.element.ElementalDamagePacket;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DamageMetadata implements Cloneable {
    private final Set<DamagePacket> packets = new HashSet<>();

    @Deprecated
    private boolean weaponCrit, skillCrit;

    @Deprecated
    private final Set<Element> elementalCrit = new HashSet<>();

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

    /**
     * Used to register a attack
     *
     * @param damage  The attack damage
     * @param element If this is an elemental attack
     * @param types   The attack damage types
     */
    public DamageMetadata(double damage, @NotNull Element element, DamageType... types) {
        packets.add(new ElementalDamagePacket(damage, element, types));
    }

    public boolean isWeaponCriticalStrike() {
        return weaponCrit;
    }

    public void registerWeaponCriticalStrike() {
        this.weaponCrit = true;
    }

    public boolean isSkillCriticalStrike() {
        return skillCrit;
    }

    public void registerSkillCriticalStrike() {
        this.skillCrit = true;
    }

    public boolean isElementalCriticalStrike(Element el) {
        return elementalCrit.contains(el);
    }

    public void registerElementalCriticalStrike(Element el) {
        elementalCrit.add(el);
    }

    public double getDamage() {
        double d = 0;

        for (DamagePacket packet : packets)
            d += packet.getFinalValue();

        return d;
    }

    public double getDamage(DamageType type) {
        double d = 0;

        for (DamagePacket packet : packets)
            if (packet.hasType(type))
                d += packet.getFinalValue();

        return d;
    }

    public Map<Element, Double> mapElementalDamage() {
        Map<Element, Double> mapped = new HashMap<>();

        for (DamagePacket packet : packets)
            if (packet instanceof ElementalDamagePacket) {
                final Element el = ((ElementalDamagePacket) packet).getElement();
                mapped.put(el, mapped.getOrDefault(el, 0d) + packet.getFinalValue());
            }

        return mapped;
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
            for (DamageType type : packet.getTypes())
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
     * @param coefficient Multiplicative coefficient. 1.5 will
     *                    increase final damage by 50%
     * @return The same damage metadata
     */
    public DamageMetadata multiplicativeModifier(double coefficient) {
        for (DamagePacket packet : packets)
            packet.multiplicativeModifier(coefficient);
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
            packet.additiveModifier(multiplier);
        return this;
    }

    /**
     * Register a multiplicative damage modifier for a specific
     * damage type.
     * <p>
     * This is not being used in MMOCore nor MMOItems
     *
     * @param coefficient Multiplicative coefficient. 1.5 will
     *                    increase final damage by 50%
     * @param concerned   Concerned damage type
     * @return The same damage metadata
     */
    public DamageMetadata multiplicativeModifier(double coefficient, @NotNull DamageType concerned) {
        for (DamagePacket packet : packets)
            if (packet.hasType(concerned))
                packet.multiplicativeModifier(coefficient);
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
                packet.additiveModifier(multiplier);
        return this;
    }

    @Override
    public DamageMetadata clone() {
        DamageMetadata clone = new DamageMetadata();

        for (DamagePacket packet : packets)
            clone.packets.add(packet.clone());

        return clone;
    }

    @Override
    public String toString() {

        StringBuilder damageTypes = new StringBuilder("\u00a73Damage Meta{");

        boolean packetAppended = false;
        for (DamagePacket packet : packets) {
            if (packetAppended) {
                damageTypes.append("\u00a73;");
            }
            packetAppended = true;

            // Damage
            damageTypes.append(packet);
        }

        // Yeah
        return damageTypes.append("\u00a73}").toString();
    }
}
