package io.lumine.mythic.lib.comp.profile;

import fr.phoenixdevt.profile.placeholder.PlaceholderRequest;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultProfileDataModule extends ProfileDataModuleImpl {
    public DefaultProfileDataModule(JavaPlugin plugin) {
        super(plugin);
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
}
