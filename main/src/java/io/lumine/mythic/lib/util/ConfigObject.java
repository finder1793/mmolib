package io.lumine.mythic.lib.util;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;

/**
 * Made to restrain the methods from the ConfigurationSection
 * class which include editing methods. Also provides util methods
 * like {@link #validateKeys(String...)} which aren't present in
 * bukkit config sections
 */
public class ConfigObject {
    private final ConfigurationSection config;

    public ConfigObject(ConfigurationSection config) {
        this.config = config;
    }

    public String getString(String key) {
        return config.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    public double getDouble(String key) {
        return config.getDouble(key);
    }

    public double getDouble(String key, double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    public DoubleFormula getDoubleFormula(String key) {
        return new DoubleFormula(getString(key));
    }

    public DoubleFormula getDoubleFormula(String key, DoubleFormula defaultValue) {
        return contains(key) ? getDoubleFormula(key) : defaultValue;
    }

    public int getInteger(String key) {
        return config.getInt(key);
    }

    public int getInteger(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public ConfigObject getObject(String key) {
        return new ConfigObject(config.getConfigurationSection(key));
    }

    public boolean contains(String key) {
        return config.contains(key);
    }

    public Set<String> getKeys() {
        return config.getKeys(false);
    }

    /**
     * Throws an IAE if any of the given key
     * is not found in the config object
     */
    public void validateKeys(String... keys) {
        for (String key : keys)
            Validate.isTrue(config.contains(key), "Could not find key '" + key + "' in config");
    }
}
