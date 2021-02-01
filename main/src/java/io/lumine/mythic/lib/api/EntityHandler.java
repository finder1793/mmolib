package io.lumine.mythic.lib.api;

import org.bukkit.entity.Entity;

/**
 * Used by MMOLib to recognize entities from other plugins which are NOT
 * DAMAGEABLE eg Citizens NPCs or pets from Pets plugins
 *
 * @author cympe
 */
public interface EntityHandler {

    /**
     * @param entity
     *            The bukit entity to check
     * @return If the entity should be considered invulnerable by MMOLib
     */
    boolean isInvulnerable(Entity entity);
}
