package io.lumine.mythic.lib.util;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class PostLoadAction {
    private ConfigurationSection config;
    private Consumer<ConfigurationSection> action;

    /**
     * Objects which must load some data afterwards, like quests which must load
     * their parent quests after all quests were initialized or player classes
     * which must load their subclasses.
     * <p>
     * The general use case is when some plugin as an object registry where the
     * configuration of the objects being registered rely on the SAME registry.
     *
     * @param action Action to be performed after loading the reference only
     */
    public PostLoadAction(@NotNull Consumer<ConfigurationSection> action) {
        this.action = Objects.requireNonNull(action, "Action cannot be null");
    }

    public void cacheConfig(@Nullable ConfigurationSection config) {
        Validate.notNull(action, "Object already post loaded");
        Validate.isTrue(this.config == null, "Config already cached");
        if (config != null) this.config = config;
    }

    @Nullable
    public ConfigurationSection getCachedConfig() {
        Validate.notNull(action, "Object already post loaded");
        return config;
    }

    public void performAction() {
        Validate.notNull(action, "Object already post loaded");
        if (config != null) action.accept(config);

        // Garbage collection
        action = null;
        config = null;
    }
}
