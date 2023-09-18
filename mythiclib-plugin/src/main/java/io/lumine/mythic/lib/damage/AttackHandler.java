package io.lumine.mythic.lib.damage;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

public interface AttackHandler {

    /**
     * @param event Damage event corresponding to the attack. Some plugins like mcMMO
     *              don't store the damager so the only way to retrieve it is through
     *              the damage event.
     * @return Information about the attack (the potential player damage source,
     *         damage types, and attack damage value).
     */
    @Nullable
    default AttackMetadata getAttack(EntityDamageEvent event) {
        return getAttack(event.getEntity());
    }

    /**
     * @param entity The entity being damaged by a specific plugin
     * @return Information about the attack (the potential player damage source,
     *         damage types, and attack damage value).
     */
    @Nullable
    @Deprecated
    default AttackMetadata getAttack(Entity entity) {
        throw new RuntimeException("Unsupported operation");
    }

    /**
     * @param entity The entity being damaged
     * @return If the entity is being damaged by a specific plugin
     */
    @Deprecated
    default boolean isAttacked(Entity entity) {
        throw new RuntimeException("Unsupported operation");
    }

    default boolean isFake(EntityDamageEvent event) { return false; }
}
