package io.lumine.mythic.lib.glow;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;

public interface GlowModule {

    public void setGlowing(Entity entity, ChatColor color);

    public void disableGlowing(Entity entity);

    /**
     * Called when MythicLib enables
     */
    public void enable();

    /**
     * Called when MythicLib disables
     */
    public void disable();
}
