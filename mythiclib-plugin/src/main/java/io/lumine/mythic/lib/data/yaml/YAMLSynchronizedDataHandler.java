package io.lumine.mythic.lib.data.yaml;

import io.lumine.mythic.lib.data.OfflineDataHolder;
import io.lumine.mythic.lib.data.SynchronizedDataHandler;
import io.lumine.mythic.lib.data.SynchronizedDataHolder;
import io.lumine.mythic.lib.util.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class YAMLSynchronizedDataHandler<H extends SynchronizedDataHolder, O extends OfflineDataHolder> implements SynchronizedDataHandler<H, O> {
    private final Plugin owning;

    public YAMLSynchronizedDataHandler(Plugin owning) {
        this.owning = Objects.requireNonNull(owning, "Plugin cannot be null");
    }

    @Deprecated
    public YAMLSynchronizedDataHandler(Plugin owning, boolean profilePlugin) {
        this(owning);
    }

    @Override
    public void saveData(@NotNull H playerData, boolean autosave) {
        // TODO config section is uselessly loaded into memory
        final ConfigFile configFile = getUserFile(playerData);
        saveInSection(playerData, configFile.getConfig());
        configFile.save();
    }

    public abstract void saveInSection(@NotNull H playerData, @NotNull ConfigurationSection config);

    @Override
    public boolean loadData(@NotNull H playerData) {
        // TODO support true/false
        loadFromSection(playerData, getUserFile(playerData).getConfig());
        return true;
    }

    public abstract void loadFromSection(@NotNull H playerData, @NotNull ConfigurationSection config);

    private ConfigFile getUserFile(@NotNull H playerData) {
        return new ConfigFile(owning, "/userdata", playerData.getEffectiveId().toString());
    }
}
