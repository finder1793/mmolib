package io.lumine.mythic.lib.module;

import io.lumine.mythic.lib.util.annotation.NotUsed;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@NotUsed
@Deprecated
public class MMOPluginImpl extends JavaPlugin {

    private final List<GeneralManager> managers = new ArrayList<>();

    public MMOPluginImpl() {
        MMOPluginRegistry.getInstance().registerPlugin(this);
    }

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

    @NotNull
    public List<GeneralManager> getManagers() {
        return managers;
    }

    public void registerManager(@NotNull GeneralManager manager) {
        Validate.isTrue(MMOPluginRegistry.getInstance().isRegistrationAllowed(), "Manager registration is not allowed");

        for (GeneralManager checked : managers)
            if (checked.getId().equals(manager.getId()))
                throw new IllegalArgumentException("A manager with the same name already exists");

        this.managers.add(manager);
    }
}