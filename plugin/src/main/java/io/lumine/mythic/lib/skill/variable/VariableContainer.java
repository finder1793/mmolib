package io.lumine.mythic.lib.skill.variable;

import org.jetbrains.annotations.Nullable;

public interface VariableContainer {

    @Nullable
    public Variable getVariable(String name);
}
