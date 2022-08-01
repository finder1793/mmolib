package io.lumine.mythic.lib.script.targeter.entity;

import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.targeter.EntityTargeter;
import io.lumine.mythic.lib.script.variable.def.EntityVariable;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;

import java.util.Arrays;
import java.util.List;

public class VariableEntityTargeter implements EntityTargeter {
    private final String[] args;

    public VariableEntityTargeter(ConfigObject config) {
        config.validateKeys("name");

        args = config.getString("name").split("\\.");
    }

    @Override
    public List<Entity> findTargets(SkillMetadata meta) {

        Variable var = meta.getCustomVariable(args[0]);
        for (int i = 1; i < args.length; i++)
            var = var.getVariable(args[i]);

        Validate.isTrue(var instanceof EntityVariable, "Variable '" + var.getName() + "' is not an entity");
        return Arrays.asList(((EntityVariable) var).getStored());
    }
}
