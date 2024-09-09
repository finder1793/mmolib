package io.lumine.mythic.lib.script.variable.def;

import io.lumine.mythic.lib.script.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.script.variable.VariableMetadata;
import io.lumine.mythic.lib.script.variable.VariableRegistry;

@VariableMetadata(name = "boolean")
public class BooleanVariable extends Variable<Boolean> {
    public static final SimpleVariableRegistry<Boolean> VARIABLE_REGISTRY = new SimpleVariableRegistry<>();

    public BooleanVariable(String name, boolean value) {
        super(name, value);
    }

    @Override
    public VariableRegistry<Variable<Boolean>> getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }
}
