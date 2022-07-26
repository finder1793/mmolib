package io.lumine.mythic.lib.api.event;

import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * This is a wrapper for bukkit damage events used
 * to provide more information on the current attack:
 * - the player attacking
 * - the player stats snapshot
 * - full info on the damage
 *
 * @author jules
 */
public class PlayerAttackEvent extends MMOPlayerDataEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final EntityDamageByEntityEvent event;
    private final AttackMetadata attack;

    /**
     * Called whenever a player deals damage to another entity.
     *
     * @param event  The corresponding damage event
     * @param attack The generated attack result which can be edited
     */
    public PlayerAttackEvent(EntityDamageByEntityEvent event, AttackMetadata attack) {
        super(attack.getData());

        this.event = event;
        this.attack = attack;
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean value) {
        event.setCancelled(value);
    }

    public AttackMetadata getAttack() {
        return attack;
    }

    public DamageMetadata getDamage() {
        return attack.getDamage();
    }

    public LivingEntity getEntity() {
        return (LivingEntity) event.getEntity();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

