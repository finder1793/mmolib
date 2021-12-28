package io.lumine.mythic.lib.skill.mechanic.variable.vector;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.mechanic.variable.VariableMechanic;
import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.def.PositionVariable;
import org.apache.commons.lang.Validate;
import org.bukkit.util.Vector;

@MechanicMetadata
public class NormalizeVectorMechanic extends VariableMechanic {
    public NormalizeVectorMechanic(ConfigObject config) {
        super(config);
    }

    @Override
    public void cast(SkillMetadata meta) {
        Variable var = meta.getVariable(getVariableName());
        Validate.isTrue(var instanceof PositionVariable, "Variable '" + getVariableName() + "' is not a vector");
        ((Vector) var.getStored()).normalize();
    }
}
