package io.lumine.mythic.lib.skill.variable.def.registry;

import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.VariableRegistry;
import io.lumine.mythic.lib.skill.variable.def.DoubleVariable;
import io.lumine.mythic.lib.skill.variable.def.StatsVariable;
import org.jetbrains.annotations.NotNull;

/**
 * The StatMap object has its own type of variable registry because
 * there is no definitive list of all the possible stats. Therefore,
 * you can't really register sub variables using registerVariable()
 */
public class StatsVariableRegistry extends VariableRegistry<StatsVariable> {

    @NotNull
    @Override
    public Variable accessVariable(@NotNull StatsVariable statsVariable, @NotNull String name) {
        return new DoubleVariable("temp", statsVariable.getStored().getStat(name.toUpperCase()));
    }

    @Override
    public boolean hasVariable(String name) {
        return true;
    }
}
