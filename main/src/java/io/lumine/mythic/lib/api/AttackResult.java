package io.lumine.mythic.lib.api;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AttackResult {
    private double damage;
    private boolean successful;

    private final Set<DamageType> damageTypes;

    /**
     * Used to register an attack with 0 damage
     *
     * @param successful
     *            If the attack is successful
     * @param types
     *            The attack damage types
     */
    public AttackResult(boolean successful, DamageType... types) {
        this(successful, 0, new HashSet<>());
    }

    /**
     * Used to register a successful attack
     *
     * @param damage
     *            The attack damage
     * @param types
     *            The attack damage types
     */
    public AttackResult(double damage, DamageType... types) {
        this(true, damage, new HashSet<>(Arrays.asList(types)));
    }

    /**
     * @param successful
     *            If the attack is successful
     * @param damage
     *            The attack damage
     * @param types
     *            The attack damage types
     */
    public AttackResult(boolean successful, double damage, DamageType... types) {
        this(successful, damage, new HashSet<>(Arrays.asList(types)));
    }

    /**
     * @param successful
     *            If the attack is successful
     * @param damage
     *            The attack damage
     * @param types
     *            The attack damage types
     */
    public AttackResult(boolean successful, double damage, Set<DamageType> types) {
        this.successful = successful;
        this.damage = damage;
        this.damageTypes = types;
    }

    public AttackResult(AttackResult result) {
        damage = result.damage;
        successful = result.successful;
        damageTypes = new HashSet<>(result.damageTypes);
    }

    /**
     * @return The damage types of this attack. Damage types determine what
     *         stats will be applied to increase/decrease the final attack value
     *         (eg if this is a magical attack, final damage will be increased
     *         by the "Magical Damage" stat).
     */
    public Set<DamageType> getTypes() {
        return damageTypes;
    }

    /**
     * @param type
     *            Damage type to check
     * @return If the attack has a specific damage type
     */
    public boolean hasType(DamageType type) {
        return damageTypes.contains(type);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public double getDamage() {
        return damage;
    }

    public AttackResult setDamage(double value) {
        damage = value;
        return this;
    }

    public AttackResult addDamage(double value) {
        damage += value;
        return this;
    }

    public AttackResult multiplyDamage(double coef) {
        damage *= coef;
        return this;
    }

    public AttackResult setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public AttackResult clone() {
        AttackResult clone;
        try {
            clone = (AttackResult) super.clone();
        } catch (CloneNotSupportedException ignored) {
            clone = this;
        }
        return new AttackResult(clone);
    }

    /**
     * Deals custom damage to an entity while registering it as player damage
     *
     * @param player
     *            The player damaging the entity
     * @param target
     *            The entity being damaged
     */
    public void damage(Player player, LivingEntity target) {
        MythicLib.plugin.getDamage().damage(player, target, this);
    }

    @Override
    public String toString() {
        return "{Damage=" + damage + ",Types=" + damageTypes.toString() + "}";
    }
}
