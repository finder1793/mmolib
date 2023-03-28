package io.lumine.mythic.lib.api.event.unlocking;

import io.lumine.mythic.lib.api.event.MMOPlayerDataEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ItemUnlockedEvent extends ItemChangeEvent {
    private static HandlerList handlers = new HandlerList();


    public ItemUnlockedEvent(MMOPlayerData playerData, String itemKey) {
        super(playerData,itemKey);
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
