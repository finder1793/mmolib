package io.lumine.mythic.lib.skill.custom.mechanic.variable.vector;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.variable.VariableMechanic;
import io.lumine.mythic.lib.skill.custom.variable.Variable;
import io.lumine.mythic.lib.skill.custom.variable.def.PositionVariable;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.apache.commons.lang.Validate;

/**
 * Copies a given vector and saves it into a
 * custom variable which can later be modified
 */
@MechanicMetadata
public class SaveVectorMechanic extends VariableMechanic {
    private final String varName;

    public SaveVectorMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("value");

        varName = config.getString("value");
    }

    @Override
    public void cast(SkillMetadata meta) {
        Variable var = meta.getCustomVariable(varName);
        Validate.isTrue(var instanceof PositionVariable, "Variable '" + var.getName() + "' is not a vector");
        getTargetVariableList(meta).registerVariable(new PositionVariable(getVariableName(), ((PositionVariable) var).getStored().clone()));
    }
}
