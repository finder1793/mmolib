package io.lumine.mythic.lib.script.mechanic.variable;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.script.variable.def.PositionVariable;
import io.lumine.mythic.lib.util.Position;

@MechanicMetadata
public class SetVectorMechanic extends VariableMechanic {
    private final DoubleFormula x, y, z;

    public SetVectorMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("x", "y", "z");

        x = new DoubleFormula(config.getString("x"));
        y = new DoubleFormula(config.getString("y"));
        z = new DoubleFormula(config.getString("z"));
    }

    @Override
    public void cast(SkillMetadata meta) {
        getTargetVariableList(meta).registerVariable(new PositionVariable(getVariableName(), new Position(meta.getSourceLocation().getWorld(), x.evaluate(meta), y.evaluate(meta), z.evaluate(meta))));
    }
}
