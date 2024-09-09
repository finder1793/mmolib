package io.lumine.mythic.lib.script.variable.def;

import io.lumine.mythic.lib.script.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.script.variable.VariableMetadata;
import io.lumine.mythic.lib.script.variable.VariableRegistry;
import org.bukkit.World;

@VariableMetadata(name = "world")
public class WorldVariable extends Variable<World> {
    public static final SimpleVariableRegistry<World> VARIABLE_REGISTRY = new SimpleVariableRegistry<>();

    static {
        VARIABLE_REGISTRY.registerVariable("time", var -> new IntegerVariable("temp", (int) var.getTime()));
        VARIABLE_REGISTRY.registerVariable("name", var -> new StringVariable("temp", var.getName()));
    }

    public WorldVariable(String name, World world) {
        super(name, world);
    }

    @Override
    public VariableRegistry<Variable<World>> getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }

    @Override
    public String toString() {
        return getStored() == null ? "None" : getStored().getName();
    }
}
