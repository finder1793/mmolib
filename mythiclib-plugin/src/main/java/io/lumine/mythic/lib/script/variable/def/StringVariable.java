package io.lumine.mythic.lib.script.variable.def;

import io.lumine.mythic.lib.script.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.script.variable.VariableMetadata;
import io.lumine.mythic.lib.script.variable.VariableRegistry;

@VariableMetadata(name = "string")
public class StringVariable extends Variable<String> {
    public static final SimpleVariableRegistry<String> VARIABLE_REGISTRY = new SimpleVariableRegistry<>();

    static {
        VARIABLE_REGISTRY.registerVariable("uppercase", str -> new StringVariable("temp", str.toUpperCase()));
        VARIABLE_REGISTRY.registerVariable("lowercase", str -> new StringVariable("temp", str.toLowerCase()));
    }

    public StringVariable(String name, String str) {
        super(name, str);
    }

    @Override
    public VariableRegistry<Variable<String>> getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }
}
