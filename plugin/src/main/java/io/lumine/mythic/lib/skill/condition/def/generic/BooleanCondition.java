package io.lumine.mythic.lib.skill.condition.def.generic;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.condition.Condition;
import io.lumine.mythic.lib.util.ConfigObject;
import io.lumine.mythic.lib.util.parser.boolalg.BooleanExpressionParser;

/**
 * Checks if the specified algebraic expression returns true
 */
public class BooleanCondition extends Condition {
    private final String formula;

    public BooleanCondition(ConfigObject config) {
        super(config);

        config.validateKeys("formula");

        formula = config.getString("formula");
    }

    @Override
    public boolean isMet(SkillMetadata meta) {
        return new BooleanExpressionParser().evaluate(meta.parseString(formula));
    }
}