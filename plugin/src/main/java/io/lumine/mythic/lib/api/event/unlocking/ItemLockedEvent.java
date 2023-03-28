package io.lumine.mythic.lib.api.event.unlocking;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ItemLockedEvent extends ItemChangeEvent {
    private static final HandlerList handlers = new HandlerList();

    public ItemLockedEvent(MMOPlayerData playerData, String itemKey) {
        super(playerData, itemKey);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
