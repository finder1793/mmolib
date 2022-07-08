package io.lumine.mythic.lib.util.configobject;

import java.util.HashSet;
import java.util.Set;

public class EmptyConfigObject implements ConfigObject {
    public static final ConfigObject INSTANCE = new EmptyConfigObject();

    @Override
    public String getString(String key) {
        throw new NullPointerException("Could not find string with key '" + key + "'");
    }

    @Override
    public String getString(String key, String defaultValue) {
        return defaultValue;
    }

    @Override
    public double getDouble(String key) {
        throw new NullPointerException("Could not find double with key '" + key + "'");
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return defaultValue;
    }

    @Override
    public int getInt(String key) {
        throw new NullPointerException("Could not find int with key '" + key + "'");
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean getBoolean(String key) {
        throw new NullPointerException("Could not find boolean with key '" + key + "'");
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public ConfigObject getObject(String key) {
        throw new NullPointerException("Could not find object with key '" + key + "'");
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public Set<String> getKeys() {
        return new HashSet<>();
    }
}
