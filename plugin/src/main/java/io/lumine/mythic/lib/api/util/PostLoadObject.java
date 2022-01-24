package io.lumine.mythic.lib.api.util;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PostLoadObject {
    @Nullable public ConfigurationSection getPostLoadConfig() { return config; }
    @Nullable private ConfigurationSection config;

    /**
     * Objects which must load some data afterwards, like quests which must load
     * their parent quests after all quests were initialized or classes which
     * must load their subclasses.
     *
     * @param config Config section being cached in
     *               memory until {@link #postLoad()} is called
     */
    public PostLoadObject(@Nullable ConfigurationSection config) {
        this.config = config;
    }

    public void postLoad() {
        if (config == null) { return; }

        whenPostLoaded(config);

        // Clean config object for garbage collection
        config = null;
    }

    protected abstract void whenPostLoaded(@NotNull ConfigurationSection config);
}
