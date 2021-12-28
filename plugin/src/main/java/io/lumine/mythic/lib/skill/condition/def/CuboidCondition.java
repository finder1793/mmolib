package io.lumine.mythic.lib.skill.condition.def;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.condition.Condition;
import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.def.PositionVariable;
import io.lumine.mythic.lib.util.Position;
import org.apache.commons.lang.Validate;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

/**
 * Checks if the skill target location is within the two boundaries
 */
public class CuboidCondition extends Condition {
    private final String varName1, varName2;
    private final boolean source;

    public CuboidCondition(ConfigObject config) {
        super(config);

        config.validateKeys("first", "second", "third");

        varName1 = config.getString("loc1");
        varName2 = config.getString("loc2");
        source = config.getBoolean("source", false);
    }

    @Override
    public boolean isMet(SkillMetadata meta) {
        Variable var1 = meta.getVariable(varName1);
        Validate.isTrue(var1 instanceof PositionVariable, "Variable '" + varName1 + "' is not a vector");
        Vector vec1 = ((Position) var1.getStored()).toVector();

        Variable var2 = meta.getVariable(varName1);
        Validate.isTrue(var2 instanceof PositionVariable, "Variable '" + varName2 + "' is not a vector");
        Vector vec2 = ((Position) var2.getStored()).toVector();

        return BoundingBox.of(vec1, vec2).contains(meta.getSkillLocation(source).toVector());
    }
}
