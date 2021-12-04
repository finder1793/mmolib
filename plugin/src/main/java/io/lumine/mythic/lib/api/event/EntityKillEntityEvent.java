package io.lumine.mythic.lib.api.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * @deprecated Replaced by {@link PlayerKillEntityEvent} which DOES
 *         provide the AttackMetadata that killed the entity. The new event
 *         also provides less confusion because the damager is always a Player
 *         whereas this event only provided a Bukkit entity without direct
 *         access to MMOPlayerData
 */
@Deprecated
public class EntityKillEntityEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Entity target;

    public EntityKillEntityEvent(Entity what, Entity target) {
        super(what);

        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
