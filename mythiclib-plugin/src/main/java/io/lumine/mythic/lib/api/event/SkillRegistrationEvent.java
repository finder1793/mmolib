package io.lumine.mythic.lib.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkillRegistrationEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    boolean clearBefore = false;

    public SkillRegistrationEvent(boolean clearBefore) {
        this.clearBefore = clearBefore;
    }

    public boolean isClearBefore(){
        return this.clearBefore;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    public void CallEvent() {
        Bukkit.getServer().getPluginManager().callEvent(this);
    }
}
