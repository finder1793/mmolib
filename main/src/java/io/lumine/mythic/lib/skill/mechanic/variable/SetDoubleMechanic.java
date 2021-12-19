package io.lumine.mythic.lib.skill.mechanic.variable;

import io.lumine.mythic.lib.util.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.variable.def.DoubleVariable;

@MechanicMetadata
public class SetDoubleMechanic extends VariableMechanic {
    private final DoubleFormula formula;

    public SetDoubleMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("value");

        formula = new DoubleFormula(config.getString("value"));
    }

    @Override
    public void cast(SkillMetadata meta) {
        getTargetVariableList(meta).registerVariable(new DoubleVariable(getVariableName(), formula.evaluate(meta)));
    }
}
