package io.lumine.mythic.lib.damage;

public enum DamageType {

    /**
     * Magic damage dealt by magic weapons or abilities
     */
    MAGIC,

    /**
     * Physical damage dealt by melee attacks or skills
     */
    PHYSICAL,

    /**
     * Damage dealt by any type of weapon
     */
    WEAPON,

    /**
     * Damage dealt by skills or abilities
     */
    SKILL,

    /**
     * Projectile based weapons or skills
     */
    PROJECTILE,

    /**
     * Hitting an enemy with bare hands
     */
    UNARMED,

    /**
     * For use with {@link io.lumine.mythic.lib.comp.mythicmobs.mechanic.MMODamageMechanic}
     * and {@link io.lumine.mythic.lib.comp.mythicmobs.condition.HasDamageTypeCondition}
     * to make on-hit skills that inflict damage but don't infinitely loop themselves.
     */
    ON_HIT,

    /**
     * For use with {@link io.lumine.mythic.lib.comp.mythicmobs.mechanic.MMODamageMechanic}
     * and {@link io.lumine.mythic.lib.comp.mythicmobs.condition.HasDamageTypeCondition}
     * to make summoner class abilities, supposing you had a system for it built with MythicMobs
     * or another plugin (GooP Maybe!??);
     */
    MINION,

    /**
     * Damage over time
     */
    DOT;

    public String getPath() {
        return name().toLowerCase();
    }

    public String getOffenseStat() {
        return name() + "_DAMAGE";
    }
}
