package io.lumine.mythic.lib.module;

import io.lumine.mythic.lib.util.annotation.NotUsed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A module is defined as a standalone feature in the plugin. It can
 * depend on other modules to operate, and can be disabled/enabled
 * at runtime based on configuration files.
 */
// TODO module should implement manager.
@NotUsed
public abstract class Module {
    protected final MMOPluginImpl plugin;
    protected final NamespacedKey key;
    protected final List<NamespacedKey> dependencies = new ArrayList<>();

    protected Module(@NotNull MMOPluginImpl plugin, @NotNull String key) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, key);
    }

    /**
     * Called when the server starts or when the plugins are loaded.
     */
    public abstract void load();

    /**
     * Called when the server starts or when the plugins are loaded.
     */
    public abstract void enable();

    /**
     * Called before reloading
     */
    public abstract void clear();

    public void addDependencies(NamespacedKey... dependencies) {
        Collections.addAll(this.dependencies, dependencies);
    }

    @NotNull
    public List<NamespacedKey> getDependencies() {
        return dependencies;
    }

    @NotNull
    public NamespacedKey getNamespacedKey() {
        return key;
    }

    @NotNull
    public MMOPluginImpl getPlugin() {
        return plugin;
    }
}
