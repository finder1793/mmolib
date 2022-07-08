package io.lumine.mythic.lib.util.configobject;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;
import java.util.Set;

public class ConfigSectionObject implements ConfigObject {
    private final ConfigurationSection config;

    public ConfigSectionObject(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public String getString(String key) {
        return Objects.requireNonNull(config.getString(key), "Could not find string with key '" + key + "'");
    }

    @Override
    public String getString(String key, String defaultValue) {
        return config.getString(key, Objects.requireNonNull(defaultValue, "Default value cannot be null"));
    }

    @Override
    public double getDouble(String key) {
        return config.getDouble(key);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    @Override
    public int getInt(String key) {
        return config.getInt(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    @Override
    public ConfigSectionObject getObject(String key) {
        return new ConfigSectionObject(Objects.requireNonNull(config.getConfigurationSection(key), "Could not find section with key '" + key + "'"));
    }

    @Override
    public boolean contains(String key) {
        return config.contains(key);
    }

    @Override
    public Set<String> getKeys() {
        return config.getKeys(false);
    }
}
