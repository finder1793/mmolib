package io.lumine.mythic.lib.comp.profile;

import fr.phoenixdevt.profile.ProfileDataModule;
import fr.phoenixdevt.profile.event.ProfileCreateEvent;
import fr.phoenixdevt.profile.event.ProfileDeleteEvent;
import fr.phoenixdevt.profile.event.ProfileUnloadEvent;
import fr.phoenixdevt.profile.placeholder.PlaceholderRequest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DefaultProfileDataModule implements ProfileDataModule, Listener {
    private final JavaPlugin plugin;

    public DefaultProfileDataModule(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public JavaPlugin getOwningPlugin() {
        return plugin;
    }

    @Override
    public boolean hasPlaceholders() {
        return false;
    }

    @Override
    public String getIdentifier() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void processPlaceholderRequest(PlaceholderRequest placeholderRequest) {
        throw new RuntimeException("Not supported");
    }

    @EventHandler
    public void onProfileCreate(ProfileCreateEvent event) {
        event.validate(this);
    }

    @EventHandler
    public void onProfileDelete(ProfileDeleteEvent event) {
        event.validate(this);
    }
}
