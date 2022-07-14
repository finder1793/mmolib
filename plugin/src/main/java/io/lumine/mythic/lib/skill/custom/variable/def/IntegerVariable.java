package io.lumine.mythic.lib.skill.custom.variable.def;

import io.lumine.mythic.lib.skill.custom.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.skill.custom.variable.Variable;
import io.lumine.mythic.lib.skill.custom.variable.VariableMetadata;
import io.lumine.mythic.lib.skill.custom.variable.VariableRegistry;

@VariableMetadata(name = "integer")
public class IntegerVariable extends Variable<Integer> {
    public static final VariableRegistry<IntegerVariable> VARIABLE_REGISTRY = new SimpleVariableRegistry();

    public IntegerVariable(String name, int value) {
        super(name, value);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }
}
