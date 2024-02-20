package io.lumine.mythic.lib.util;

import org.bukkit.plugin.java.JavaPlugin;

public class MMOPlugin extends JavaPlugin {

    /**
     * Does this plugin store data? This determines if MythicLib
     * must wait for this plugin to mark his data as synchronized
     * before marking the MMOPlayerData instance as fully synchronized.
     */
    public boolean hasData() {
        return true;
    }

    /**
     * It is plugin a profile plugin
     */
    public boolean hasProfiles() {
        return false;
    }
}
