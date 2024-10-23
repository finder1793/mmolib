package io.lumine.mythic.lib.module;

import io.lumine.mythic.lib.util.annotation.NotUsed;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * The main problem is loading managers on startup with the right order.
 * Managers have different behaviours on plugin startup and reload.
 * <p>
 * On startup, they usually preload object references and temporarily
 * save configuration files before loading objects further.
 *
 * @see io.lumine.mythic.lib.util.PreloadedObject
 */
@NotUsed
public abstract class GeneralManager {
    private final List<String> dependencies = new ArrayList<>();

    public void validateDependencies(@NotNull Set<String> activePlugins) {
        Validate.isTrue(MMOPluginRegistry.getInstance().isRegistrationAllowed(), "Dependency validation is not allowed");

        dependencies.removeIf(dep -> {
            final String pluginId = dep.split("\\:")[0];
            return !activePlugins.contains(pluginId);
        });
    }

    /**
     * Preparing a manager refers to instantiating all the necessary
     * references potentially needed everywhere else.
     */
    public abstract void prepare();

    public abstract void enable();

    /**
     * Called before reloading the full MMO plugins.
     */
    public abstract void clear();

    /**
     * Only reload this specific manager. It could point to outdated references
     * to other managers though, so it is recommended to fully reload all the
     * MMO plugins when doing big changes. This can be done by using /ml reload
     */
    public void reload() {
        clear();
        prepare();
        enable();
    }
}
