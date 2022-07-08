package io.lumine.mythic.lib.util.configobject;

import io.lumine.mythic.lib.util.DoubleFormula;
import org.apache.commons.lang.Validate;

import java.util.Objects;
import java.util.Set;

/**
 * An interface to cover both configuration sections and line configs.
 * <p>
 * That way there are two formats for creating a skill, either using
 * configuration sections (pro is that it takes more space and looks
 * less crammed, more familiar with SkillAPI) or line configs (for
 * users more familiar with MM)
 * <p>
 * This is also used to restrain the methods from the ConfigurationSection
 * class which include editing methods. Also provides util methods
 * like {@link #validateKeys(String...)} which aren't present in
 * bukkit config sections
 * <p>
 * There are always two methods to get a primitive with some key,
 * one method with a default value and another method which throws
 * a NPE if the returned object is null. The ..OrDefault method always
 * takes one extra map checkup so it's best to use it on startup only.
 *
 * @author jules
 */
public interface ConfigObject {
    String getString(String key);

    String getString(String key, String defaultValue);

    double getDouble(String key);

    double getDouble(String key, double defaultValue);

    int getInt(String key);

    int getInt(String key, int defaultValue);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defaultValue);

    default DoubleFormula getDoubleFormula(String key) {
        return new DoubleFormula(getString(key));
    }

    default DoubleFormula getDoubleFormula(String key, DoubleFormula defaultValue) {
        return contains(key) ? getDoubleFormula(key) : Objects.requireNonNull(defaultValue, "Default value cannot be null");
    }

    ConfigObject getObject(String key);

    boolean contains(String key);

    Set<String> getKeys();

    /**
     * Throws an IAE if any of the given key
     * is not found in the config object
     */
    default void validateKeys(String... keys) {
        for (String key : keys)
            Validate.isTrue(contains(key), "Could not find key '" + key + "' in config");
    }

    /**
     * Throws IAE if the config has less than X parameters
     *
     * @param count The amount of arguments
     */
    default void validateArgs(int count) {
        Validate.isTrue(getKeys().size() >= count, "Config must have at least " + count + " parameters");
    }
}
