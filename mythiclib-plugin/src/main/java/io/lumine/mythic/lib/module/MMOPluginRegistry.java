package io.lumine.mythic.lib.module;

import io.lumine.mythic.lib.util.annotation.NotUsed;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NotUsed
@Deprecated
public class MMOPluginRegistry {
    private final List<MMOPluginImpl> plugins = new ArrayList<>();

    /**
     * Sorted list including all managers from all registered MMO plugins
     */
    private final List<GeneralManager> sorted = new ArrayList<>();

    private boolean registration = true;

    private static MMOPluginRegistry instance;

    private MMOPluginRegistry() {
        // Singleton
    }

    public static MMOPluginRegistry getInstance() {
        if (instance == null) instance = new MMOPluginRegistry();
        return instance;
    }

    public void registerPlugin(@NotNull MMOPluginImpl plugin) {
        this.plugins.add(plugin);
    }

    public boolean isRegistrationAllowed() {
        return registration;
    }

    public void prepareManagers() {
        Validate.isTrue(registration, "Managers were already prepared");

        // Validate all managers
        final Set<String> activePluginIds = plugins.stream().map(plugin -> lowerCase(plugin.getName())).collect(Collectors.toSet());
        plugins.forEach(plugin -> plugin.getManagers().forEach(manager -> manager.validateDependencies(activePluginIds)));

        // Topological sorting
        sortManagers();

        // Prepare all managers


        registration = false;
    }
    private void sortManagers() {
        // TODO topological sorting of all managers based on dependencies
    }

    @NotNull
    private static String lowerCase(@NotNull String str) {
        return str.toLowerCase().replace(" ", "_").replace("-", "_").replaceAll("[^a-z_]", "");
    }
}
