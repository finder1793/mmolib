package io.lumine.mythic.lib.skill.variable.def;

import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.VariableMetadata;
import io.lumine.mythic.lib.skill.variable.VariableRegistry;
import org.bukkit.World;

@VariableMetadata(name = "world")
public class WorldVariable extends Variable<World> {
    public static final VariableRegistry<WorldVariable> VARIABLE_REGISTRY = new VariableRegistry();

    static {
        VARIABLE_REGISTRY.registerVariable("time", var -> new IntegerVariable("temp", (int) var.getStored().getTime()));
    }

    public WorldVariable(String name, World world) {
        super(name, world);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }

    @Override
    public String toString() {
        return getStored() == null ? "None" : getStored().getName();
    }
}
