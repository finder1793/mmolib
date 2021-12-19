package io.lumine.mythic.lib.skill.variable.def.registry;

import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.VariableRegistry;
import io.lumine.mythic.lib.skill.variable.def.CooldownsVariable;
import io.lumine.mythic.lib.skill.variable.def.DoubleVariable;
import org.jetbrains.annotations.NotNull;

/**
 * The CooldownMap object has its own type of variable registry because
 * there is no definitive list of all the possible cooldown paths. Therefore,
 * you can't really register sub variables using registerVariable()
 */
public class CooldownsVariableRegistry extends VariableRegistry<CooldownsVariable> {

    @NotNull
    @Override
    public Variable accessVariable(@NotNull CooldownsVariable cdVariable, @NotNull String name) {
        return new DoubleVariable("temp", cdVariable.getStored().getCooldown(name));
    }

    @Override
    public boolean hasVariable(String name) {
        return true;
    }
}
