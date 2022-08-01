package io.lumine.mythic.lib.script.condition.generic;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.condition.Condition;

/**
 * Checks if the two strings are equal
 */
public class StringEqualsCondition extends Condition {
    private final String first, second;
    private final boolean ignoreCase;

    public StringEqualsCondition(ConfigObject config) {
        super(config);

        config.validateKeys("first", "second");

        first = config.getString("first");
        second = config.getString("second");
        ignoreCase = config.getBoolean("ignore_case", false);
    }

    @Override
    public boolean isMet(SkillMetadata meta) {
        return ignoreCase ?
                meta.parseString(first).equalsIgnoreCase(meta.parseString(second))
                : meta.parseString(first).equals(meta.parseString(second));
    }
}
