package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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
        for (DamagePacket packet : packets)
            d += packet.getValue();
        return d;
    }

    public Set<DamagePacket> getPackets() {
        return packets;
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
     * Multiply all registered damage packets
     *
     * @param coef Multiplicative coefficient
     * @return
     */
    public DamageMetadata multiply(double coef) {
        for (DamagePacket packet : packets)
            packet.multiplyValue(coef);
        return this;
    }

    /**
     * Multiply damage from a specific source only
     *
     * @param coef      Multiplicative coefficient
     * @param concerned Concerned damage type
     * @return
     */
    public DamageMetadata multiply(double coef, DamageType concerned) {
        for (DamagePacket packet : packets)
            if (packet.hasType(concerned))
                packet.multiplyValue(coef);
        return this;
    }

    /**
     * Deals custom damage to an entity while registering it as player damage
     *
     * @param player
     *            The player damaging the entity
     * @param target
     *            The entity being damaged
     */
    @Deprecated
    public void damage(Player player, LivingEntity target) {
        MythicLib.plugin.getDamage().damage(player, target, this);
    }

    @Override
    public DamageMetadata clone() {
        DamageMetadata clone = new DamageMetadata();
        for (DamagePacket packet : packets)
            clone.packets.add(packet.clone());

        return clone;
    }
}