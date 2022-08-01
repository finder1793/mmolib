package io.lumine.mythic.lib.script.variable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class VariableList implements VariableContainer {
    private final VariableScope scope;
    private final Map<String, Variable> vars = new HashMap<>();

    public VariableList(VariableScope scope) {
        this.scope = scope;
    }

    public VariableScope getScope() {
        return scope;
    }

    @Nullable
    public Variable getVariable(String name) {
        return vars.get(name);
    }

    public void registerVariable(Variable var) {
        vars.put(var.getName(), var);
    }
}
