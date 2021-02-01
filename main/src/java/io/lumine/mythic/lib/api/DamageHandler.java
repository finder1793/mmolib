package io.lumine.mythic.lib.api;

import org.bukkit.entity.Entity;

public interface DamageHandler {

    /**
     *
     * @param entity
     *            The entity being damaged by a specific plugin
     * @return Information about the attack (the potential player damage source,
     *         damage types, and attack damage value).
     */
    RegisteredAttack getDamage(Entity entity);

    /**
     * @param entity
     *            The entity being damaged
     * @return If the entity is being damaged by a specific plugin
     */
    boolean hasDamage(Entity entity);
}
