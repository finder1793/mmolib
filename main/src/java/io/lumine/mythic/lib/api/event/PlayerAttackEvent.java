package io.lumine.mythic.lib.api.event;

import io.lumine.mythic.lib.api.AttackResult;
import io.lumine.mythic.lib.api.DamageType;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerAttackEvent extends MMOPlayerDataEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final EntityDamageByEntityEvent event;
    private final AttackResult attack;

    /**
     * Called whenever a player deals damage to another entity.
     *
     * @param data
     *            the MMOPlayerData of the player damager
     * @param event
     *            The corresponding damage event
     * @param attack
     *            The generated attack result which can be edited
     */
    public PlayerAttackEvent(MMOPlayerData data, EntityDamageByEntityEvent event, AttackResult attack) {
        super(data);

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

    public AttackResult getAttack() {
        return attack;
    }

    /***
     * @return If the event is called because of a melee player attack
     * @deprecated Old method, use getAttack().getTypes().contains(DamageType)
     *             which works with other damage types.
     */
    @Deprecated
    public boolean isWeapon() {
        return attack.getTypes().contains(DamageType.WEAPON);
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

