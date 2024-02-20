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
        final ConfigFile configFile = getUserFile(playerData);
        final ConfigFile officialUUIDConfigFile = getEntityUUIDConfigFile(configFile, playerData);
        saveInSection(playerData, configFile.getConfig(), officialUUIDConfigFile.getConfig());
        configFile.save();
        if (officialUUIDConfigFile != configFile)
            officialUUIDConfigFile.save();
    }

    public abstract void saveInSection(@NotNull H playerData, @NotNull ConfigurationSection config);

    /**
     * Override this method if you want to make a mix between profile specific data and profile independent data.
     *
     * @param playerData        The player Data to loadData from
     * @param config            The config corresponding to the effective UUID of the player. (Its profile UUID if MMOProfiles is on)
     * @param entityUUIDSection The config corresponding to the official UUID of the player. (Its real in game UUID)
     */
    public void saveInSection(@NotNull H playerData, @NotNull ConfigurationSection config, @NotNull ConfigurationSection entityUUIDSection) {
        saveInSection(playerData, config);
    }

    @Override
    public boolean loadData(@NotNull H playerData) {
        loadFromSection(playerData, getUserFile(playerData).getConfig());
        ConfigFile configFile = getUserFile(playerData);
        loadFromSection(playerData, configFile.getConfig(), getEntityUUIDConfigFile(configFile, playerData).getConfig());
        return true;
    }

    public abstract void loadFromSection(@NotNull H playerData, @NotNull ConfigurationSection config);

    /**
     * Override this method if you want to make a mix between profile specific data and profile independent data.
     * This method will handle all the profile independent data.
     *
     * @param playerData         The player Data to loadData from
     * @param officialUUIDConfig The config corresponding to the official UUID of the player. (Its real in game UUID)
     *                           This is null if the effective UUID is the same as the official UUID.
     * @NotNull ConfigurationSection config
     */
    public void loadFromSection(@NotNull H playerData, @NotNull ConfigurationSection config, @NotNull ConfigurationSection officialUUIDConfig) {
        loadFromSection(playerData, config);
    }

    private ConfigFile getUserFile(@NotNull H playerData) {
        return new ConfigFile(owning, "/userdata", playerData.getEffectiveId().toString());
    }

    private ConfigFile getEntityUUIDConfigFile(@NotNull ConfigFile configFile, @NotNull H playerData) {
        if (playerData.getEffectiveId().equals(playerData.getUniqueId()))
            return configFile;
        return new ConfigFile(owning, "/userdata", playerData.getUniqueId().toString());
    }
}
