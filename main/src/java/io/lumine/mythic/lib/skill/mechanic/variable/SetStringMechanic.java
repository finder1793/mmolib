package io.lumine.mythic.lib.skill.mechanic.variable;

import io.lumine.mythic.lib.util.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.variable.def.StringVariable;

@MechanicMetadata
public class SetStringMechanic extends VariableMechanic {
    private final String value;

    public SetStringMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("value");

        value = config.getString("value");
    }

    @Override
    public void cast(SkillMetadata meta) {
        getTargetVariableList(meta).registerVariable(new StringVariable(getVariableName(), meta.parseString(value)));
    }
}
