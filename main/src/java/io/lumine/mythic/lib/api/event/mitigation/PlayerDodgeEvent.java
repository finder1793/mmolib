package io.lumine.mythic.lib.api.event.mitigation;

import io.lumine.mythic.lib.api.event.MMOPlayerDataEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDodgeEvent extends MMOPlayerDataEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final EntityDamageEvent event;
    private boolean cancelled;

    public PlayerDodgeEvent(MMOPlayerData player, EntityDamageEvent event) {
        super(player);

        this.event = event;
    }

    public EntityDamageEvent getEvent() {
        return event;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

