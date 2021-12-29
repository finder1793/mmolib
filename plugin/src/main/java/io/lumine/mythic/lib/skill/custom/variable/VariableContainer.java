package io.lumine.mythic.lib.skill.custom.variable;

import org.jetbrains.annotations.Nullable;

public interface VariableContainer {

    @Nullable
    public Variable getVariable(String name);
}
