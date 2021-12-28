package io.lumine.mythic.lib.util.configobject;

import io.lumine.mythic.lib.api.MMOLineConfig;

import java.util.Objects;
import java.util.Set;

public class LineConfigObject implements ConfigObject {
    private final MMOLineConfig lineConfig;

    public LineConfigObject(MMOLineConfig lineConfig) {
        this.lineConfig = lineConfig;
    }

    @Override
    public String getString(String key) {
        return Objects.requireNonNull(lineConfig.getString(key), "Could not find string with key '" + key + "'");
    }

    @Override
    public String getString(String key, String defaultValue) {
        return lineConfig.getString(key, Objects.requireNonNull(defaultValue, "Default value cannot be null"));
    }

    @Override
    public double getDouble(String key) {
        return lineConfig.getDouble(key);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return lineConfig.getDouble(key, defaultValue);
    }

    @Override
    public int getInteger(String key) {
        return lineConfig.getInt(key);
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        return lineConfig.getInt(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return lineConfig.getBoolean(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return lineConfig.getBoolean(key, defaultValue);
    }

    @Override
    public ConfigObject getObject(String key) {
        return new LineConfigObject(lineConfig.getConfig(key));
    }

    @Override
    public boolean contains(String key) {
        return lineConfig.contains(key);
    }

    @Override
    public Set<String> getKeys() {
        return lineConfig.getKeys();
    }
}
