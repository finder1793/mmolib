package io.lumine.mythic.lib.api.event;

import io.lumine.mythic.lib.data.SynchronizedDataHolder;
import io.lumine.mythic.lib.data.SynchronizedDataManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncSynchronizedDataLoadEvent extends Event {
    private final SynchronizedDataManager<?, ?> manager;
    private final SynchronizedDataHolder holder;

    private static final HandlerList HANDLERS = new HandlerList();

    public AsyncSynchronizedDataLoadEvent(SynchronizedDataManager<?, ?> manager, SynchronizedDataHolder holder) {
        super(true);

        this.holder = holder;
        this.manager = manager;
    }

    public SynchronizedDataManager<?, ?> getManager() {
        return manager;
    }

    public SynchronizedDataHolder getHolder() {
        return holder;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
