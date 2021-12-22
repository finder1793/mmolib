package io.lumine.mythic.lib.api.event;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.player.TemporaryPlayerData;
import org.bukkit.event.HandlerList;

/**
 * Deals with the issue of plugins having to unload player data
 * on player disconnection to fix memory leaks. Some plugins like
 * MMOCore lose some temporary data that does not get stored in
 * databases like cooldowns, temporary buffs or custom resources
 * like mana, stamina....
 * <p>
 * This event passes an instance of {@link TemporaryPlayerData} which
 * contains all that otherwise-lost information.
 * <p>
 * The aim of that event is to provide the plugins a way to save
 * that data before everything is unloaded on player disconnection,
 * that is why this event must be used with the LOWEST priority.
 *
 * @author jules
 */
public class TemporaryDataSavedEvent extends MMOPlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    private final TemporaryPlayerData tempData;

    /**
     * Called whenever a player is logging off. Generating the
     * event instance also generates the {@link TemporaryPlayerData} instance
     * which can be accessed using
     *
     * @param data the MMOPlayerData of the player damager
     */
    public TemporaryDataSavedEvent(MMOPlayerData data) {
        super(data);

        this.tempData = new TemporaryPlayerData(data);
    }

    public TemporaryPlayerData getTemporaryData() {
        return tempData;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

