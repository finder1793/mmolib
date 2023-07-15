package io.lumine.mythic.lib.api.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class IndicatorDisplayEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private String message;
    private boolean cancelled;

    private final IndicatorType type;

    /**
     * Called when an entity emits either a damage or a healing indicator.
     *
     * @param entity  Entity emitting the indicator
     * @param message Message displayed
     * @param type    Type of indicator, either DAMAGE or REGENERATION
     */
    public IndicatorDisplayEvent(@NotNull Entity entity, String message, IndicatorType type) {
        super(entity);

        this.message = message;
        this.type = type;
    }

    public IndicatorType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Changes the message displayed by the indicator
     *
     * @param message Message to display
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum IndicatorType {

        /**
         * Displayed when an entity is being damaged
         */
        DAMAGE,

        /**
         * Displayed when an entity regenerates some health
         */
        REGENERATION;
    }
}
