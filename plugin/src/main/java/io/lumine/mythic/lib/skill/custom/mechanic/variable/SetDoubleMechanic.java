package io.lumine.mythic.lib.skill.custom.mechanic.variable;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.custom.variable.def.DoubleVariable;

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
