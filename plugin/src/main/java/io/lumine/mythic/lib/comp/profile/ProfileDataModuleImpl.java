package io.lumine.mythic.lib.comp.profile;

import fr.phoenixdevt.profile.ProfileDataModule;
import io.lumine.mythic.lib.data.SynchronizedDataManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Used by plugins like MMOInventory which support profile-based data
 * but have no placeholders. This class is absolutely necessary to make
 * sure the class {@link SynchronizedDataManager} contains no reference
 * to MMOProfiles code.
 */
public abstract class ProfileDataModuleImpl implements ProfileDataModule {
    private final JavaPlugin plugin;

    public ProfileDataModuleImpl(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public JavaPlugin getOwningPlugin() {
        return plugin;
    }
}
