package io.lumine.mythic.lib.script.mechanic.variable;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.script.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.script.variable.def.BooleanVariable;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.util.configobject.ConfigObject;

@MechanicMetadata
public class SetBooleanMechanic extends VariableMechanic {
    private final String expression;

    public SetBooleanMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("value");

        expression = config.getString("value");
    }

    @Override
    public void cast(SkillMetadata meta) {
        final boolean value = MythicLib.plugin.getFormulaParser().evaluateAs(meta.parseString(expression), Boolean.class).booleanValue();
        getTargetVariableList(meta).registerVariable(new BooleanVariable(getVariableName(), value));
    }
}
