package io.lumine.mythic.lib.script.mechanic.variable;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.script.variable.def.StringVariable;

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
