package io.lumine.mythic.lib.skill.variable.def;

import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.VariableMetadata;
import io.lumine.mythic.lib.skill.variable.VariableRegistry;

@VariableMetadata(name = "string")
public class StringVariable extends Variable<String> {
    public static final VariableRegistry<StringVariable> VARIABLE_REGISTRY = new VariableRegistry<>();

    public StringVariable(String name, String str) {
        super(name, str);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }
}
