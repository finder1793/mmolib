package io.lumine.mythic.lib.api;

import io.lumine.mythic.lib.util.configobject.JsonWrapper;

public class MMOLineConfig extends JsonWrapper {
    private final String key;
    private final String[] args;

    /**
     * Reads a JSON config from a string line. These are used everywhere in
     * MMOItems/MMOCore configs to register quest triggers, objectives, crafting
     * ingredients... Throws IAE
     *
     * @param value The string to parse
     */
    public MMOLineConfig(String value) {
        super(value);

        /*
         * If there is no config, no need to parse the Json object.
         * Split, define key and find arguments
         */
        if (!value.contains("{") || !value.contains("}")) {
            String[] split = value.split(" ");
            key = split[0];
            args = split.length > 1 ? value.replace(key + " ", "").split(" ") : new String[0];
            return;
        }

        // Load Json object and extra arguments
        final int begin = value.indexOf("{"), end = value.lastIndexOf("}") + 1;
        key = value.substring(0, begin);

        final String format = value.substring(Math.min(value.length(), end + 1));
        args = format.isEmpty() ? new String[0] : format.split(" ");
    }

    /**
     * @return Extra arguments outside the config brackets. These are used for
     *         instance in MMOCore drop items for drop chance, item amounts and
     *         drop item weights.
     */
    public String[] args() {
        return args;
    }

    /**
     * @return The string key in front of the brackets
     */
    public String getKey() {
        return key;
    }

    /**
     * Throws IAE if the config is missing any of these paths
     *
     * @param paths The config paths to check
     */
    @Deprecated
    public void validate(String... paths) {
        validateKeys(paths);
    }

    @Override
    public String toString() {
        return key + object.toString();
    }
}
