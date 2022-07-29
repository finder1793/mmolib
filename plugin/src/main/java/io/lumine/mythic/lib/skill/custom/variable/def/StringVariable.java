package io.lumine.mythic.lib.skill.custom.variable.def;

import io.lumine.mythic.lib.skill.custom.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.skill.custom.variable.Variable;
import io.lumine.mythic.lib.skill.custom.variable.VariableMetadata;
import io.lumine.mythic.lib.skill.custom.variable.VariableRegistry;

@VariableMetadata(name = "string")
public class StringVariable extends Variable<String> {
    public static final SimpleVariableRegistry<StringVariable> VARIABLE_REGISTRY = new SimpleVariableRegistry<>();

    static {
        VARIABLE_REGISTRY.registerVariable("uppercase", str -> new StringVariable("temp", str.getStored().toUpperCase()));
        VARIABLE_REGISTRY.registerVariable("lowercase", str -> new StringVariable("temp", str.getStored().toLowerCase()));
    }

    public StringVariable(String name, String str) {
        super(name, str);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }
}
