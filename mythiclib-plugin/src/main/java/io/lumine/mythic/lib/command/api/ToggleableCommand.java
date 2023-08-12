package io.lumine.mythic.lib.command.api;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ToggleableCommand {

    private final String name, description, permission;
    private final Function<ConfigurationSection, CommandTreeRoot> generator;
    private final List<String> aliases;
    private final Predicate<Void> enabled;

    public ToggleableCommand(@NotNull String name, String permission, @NotNull String description, @NotNull Function<ConfigurationSection, CommandTreeRoot> generator, @NotNull String... aliases) {
        this(name, permission, description, generator, null, aliases);
    }

    public ToggleableCommand(@NotNull String name, String permission, @NotNull String description, @NotNull Function<ConfigurationSection, CommandTreeRoot> generator, @Nullable Predicate<Void> enabled, @NotNull String... aliases) {
        this.name = name;
        this.description = description;
        this.generator = generator;
        this.aliases = Arrays.asList(aliases);
        this.permission = permission;
        this.enabled = enabled == null ? v -> true : enabled;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getPermission() {
        return permission;
    }

    public String getConfigPath() {
        return name.toLowerCase().replace("_", "-");
    }

    public CommandTreeRoot generate(ConfigurationSection config) {
        return generator.apply(config);
    }

    public boolean isEnabled() {
        return enabled.test(null);
    }
}
