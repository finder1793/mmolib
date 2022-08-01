package io.lumine.mythic.lib.script.variable.def;

import io.lumine.mythic.lib.script.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.script.variable.VariableMetadata;
import io.lumine.mythic.lib.script.variable.VariableRegistry;
import org.bukkit.World;

@VariableMetadata(name = "world")
public class WorldVariable extends Variable<World> {
    public static final SimpleVariableRegistry<WorldVariable> VARIABLE_REGISTRY = new SimpleVariableRegistry();

    static {
        VARIABLE_REGISTRY.registerVariable("time", var -> new IntegerVariable("temp", (int) var.getStored().getTime()));
        VARIABLE_REGISTRY.registerVariable("name", var -> new StringVariable("temp", var.getStored().getName()));
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
