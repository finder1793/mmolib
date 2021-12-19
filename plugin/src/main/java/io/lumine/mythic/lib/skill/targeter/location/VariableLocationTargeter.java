package io.lumine.mythic.lib.skill.targeter.location;

import io.lumine.mythic.lib.util.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.targeter.LocationTargeter;
import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.def.PositionVariable;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

public class VariableLocationTargeter implements LocationTargeter {
    private final String[] args;

    public VariableLocationTargeter(ConfigObject config) {
        config.validateKeys("name");

        args = config.getString("name").split("\\.");
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {

        Variable var = meta.getCustomVariable(args[0]);
        for (int i = 1; i < args.length; i++)
            var = var.getVariable(args[i]);

        Validate.isTrue(var instanceof PositionVariable, "Variable '" + var.getName() + "' is not a vector");
        return Arrays.asList(((PositionVariable) var).getStored().toLocation());
    }
}
