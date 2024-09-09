package io.lumine.mythic.lib.api.event;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLogoutEvent extends MMOPlayerDataEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerLogoutEvent(@NotNull MMOPlayerData playerData) {
        super(playerData);
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
