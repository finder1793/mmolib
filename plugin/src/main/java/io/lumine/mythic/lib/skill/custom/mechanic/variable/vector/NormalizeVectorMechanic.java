package io.lumine.mythic.lib.skill.custom.mechanic.variable.vector;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.variable.VariableMechanic;
import io.lumine.mythic.lib.skill.custom.variable.Variable;
import io.lumine.mythic.lib.skill.custom.variable.def.PositionVariable;
import io.lumine.mythic.lib.util.Position;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.apache.commons.lang.Validate;

@MechanicMetadata
public class NormalizeVectorMechanic extends VariableMechanic {
    public NormalizeVectorMechanic(ConfigObject config) {
        super(config);
    }

    @Override
    public void cast(SkillMetadata meta) {
        Variable var = meta.getCustomVariable(getVariableName());
        Validate.isTrue(var instanceof PositionVariable, "Variable '" + getVariableName() + "' is not a vector");
        ((Position) var.getStored()).normalize();
    }
}
