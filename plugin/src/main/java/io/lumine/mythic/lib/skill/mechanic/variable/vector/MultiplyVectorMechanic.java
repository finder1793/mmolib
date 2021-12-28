package io.lumine.mythic.lib.skill.mechanic.variable.vector;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.mechanic.variable.VariableMechanic;
import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.def.PositionVariable;
import org.apache.commons.lang.Validate;

@MechanicMetadata
public class MultiplyVectorMechanic extends VariableMechanic {
    private final DoubleFormula coef;

    public MultiplyVectorMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("coef");

        coef = new DoubleFormula(config.getString("coef"));
    }

    @Override
    public void cast(SkillMetadata meta) {
        Variable var = meta.getVariable(getVariableName());
        Validate.isTrue(var instanceof PositionVariable, "Variable '" + getVariableName() + "' is not a vector");
        ((PositionVariable) var).getStored().multiply(coef.evaluate(meta));
    }
}
