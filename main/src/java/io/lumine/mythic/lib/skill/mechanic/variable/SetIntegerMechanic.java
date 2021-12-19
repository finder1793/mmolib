package io.lumine.mythic.lib.skill.mechanic.variable;

import io.lumine.mythic.lib.util.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.variable.def.IntegerVariable;

@MechanicMetadata
public class SetIntegerMechanic extends VariableMechanic {
    private final DoubleFormula formula;

    public SetIntegerMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("value");

        formula = new DoubleFormula(config.getString("value"));
    }

    @Override
    public void cast(SkillMetadata meta) {
        getTargetVariableList(meta).registerVariable(new IntegerVariable(getVariableName(), (int) formula.evaluate(meta)));
    }
}
