package io.lumine.mythic.lib.skill.custom.variable.def;

import io.lumine.mythic.lib.skill.custom.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.skill.custom.variable.Variable;
import io.lumine.mythic.lib.skill.custom.variable.VariableMetadata;
import io.lumine.mythic.lib.skill.custom.variable.VariableRegistry;

@VariableMetadata(name = "double")
public class DoubleVariable extends Variable<Double> {
    public static final SimpleVariableRegistry<DoubleVariable> VARIABLE_REGISTRY = new SimpleVariableRegistry();

    static {
        VARIABLE_REGISTRY.registerVariable("int", var -> new IntegerVariable("temp", (int) (double) var.getStored()));
    }

    public DoubleVariable(String name, double value) {
        super(name, value);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }
}
