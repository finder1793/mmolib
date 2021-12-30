package io.lumine.mythic.lib.api.util;

import org.bukkit.configuration.ConfigurationSection;

public abstract class PostLoadObject {
    private ConfigurationSection config;

    /**
     * Objects which must load some data afterwards, like quests which must load
     * their parent quests after all quests were initialized or classes which
     * must load their subclasses.
     *
     * @param config Config section being cached in
     *               memory until {@link #postLoad()} is called
     */
    public PostLoadObject(ConfigurationSection config) {
        this.config = config;
    }

    public void postLoad() {
        if (config == null)
            return;

        whenPostLoaded(config);

        // Clean config object for garbage collection
        config = null;
    }

    protected abstract void whenPostLoaded(ConfigurationSection config);
}
