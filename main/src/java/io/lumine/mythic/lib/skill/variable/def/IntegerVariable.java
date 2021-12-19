package io.lumine.mythic.lib.skill.variable.def;

import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.VariableMetadata;
import io.lumine.mythic.lib.skill.variable.VariableRegistry;

@VariableMetadata(name = "integer")
public class IntegerVariable extends Variable<Integer> {
    public static final VariableRegistry<IntegerVariable> VARIABLE_REGISTRY = new VariableRegistry();

    public IntegerVariable(String name, int value) {
        super(name, value);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }
}
