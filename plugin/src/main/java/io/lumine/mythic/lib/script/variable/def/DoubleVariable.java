package io.lumine.mythic.lib.script.variable.def;

import io.lumine.mythic.lib.script.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.script.variable.VariableMetadata;
import io.lumine.mythic.lib.script.variable.VariableRegistry;

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
