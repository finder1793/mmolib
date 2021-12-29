package io.lumine.mythic.lib.skill.custom.mechanic.variable;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.Mechanic;
import io.lumine.mythic.lib.skill.custom.variable.VariableList;
import io.lumine.mythic.lib.skill.custom.variable.VariableScope;

/**
 * Mechanic that affects a new value to a variable. It is
 * possible to choose what scope to set for the modified variable
 */
public abstract class VariableMechanic extends Mechanic {
    private final String varName;
    private final VariableScope scope;

    public VariableMechanic(ConfigObject config) {
        config.validateKeys("variable");

        varName = config.getString("variable");
        scope = config.contains("scope") ? VariableScope.valueOf(config.getString("scope").toUpperCase()) : VariableScope.SKILL;
    }

    public String getVariableName() {
        return varName;
    }

    public VariableList getTargetVariableList(SkillMetadata meta) {
        return scope == VariableScope.SKILL ? meta.getVariableList() : meta.getCaster().getSkillVariableList();
    }
}
