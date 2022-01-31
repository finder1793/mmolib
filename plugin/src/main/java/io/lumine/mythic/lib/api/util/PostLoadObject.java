package io.lumine.mythic.lib.api.util;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class PostLoadObject {

    @Nullable
    private ConfigurationSection config;

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

    private static final String ERROR_MESSAGE = "Objet already post loaded";

    @NotNull
    public ConfigurationSection getPostLoadConfig() {
        return Objects.requireNonNull(config, ERROR_MESSAGE);
    }

    public void postLoad() {
        Validate.notNull(config, ERROR_MESSAGE);
        whenPostLoaded(config);
        config = null;
    }

    protected abstract void whenPostLoaded(@NotNull ConfigurationSection config);
}
