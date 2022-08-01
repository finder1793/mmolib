package io.lumine.mythic.lib.script.condition.generic;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.condition.Condition;

/**
 * Checks if a given double is within some range.
 * The first value is included, the second is excluded.
 */
public class InBetweenCondition extends Condition {
    private final DoubleFormula first, second, third;

    public InBetweenCondition(ConfigObject config) {
        super(config);

        config.validateKeys("first", "second", "third");

        first = new DoubleFormula(config.getString("first"));
        second = new DoubleFormula(config.getString("second"));
        third = new DoubleFormula(config.getString("third"));
    }

    @Override
    public boolean isMet(SkillMetadata meta) {
        double middle = second.evaluate(meta);
        return first.evaluate(meta) <= middle && middle < third.evaluate(meta);
    }
}
