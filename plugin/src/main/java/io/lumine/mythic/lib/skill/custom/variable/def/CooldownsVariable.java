package io.lumine.mythic.lib.skill.custom.variable.def;

import io.lumine.mythic.lib.player.cooldown.CooldownMap;
import io.lumine.mythic.lib.skill.custom.variable.Variable;
import io.lumine.mythic.lib.skill.custom.variable.VariableMetadata;
import io.lumine.mythic.lib.skill.custom.variable.VariableRegistry;
import io.lumine.mythic.lib.skill.custom.variable.def.registry.CooldownsVariableRegistry;

@VariableMetadata(name = "cooldownMap")
public class CooldownsVariable extends Variable<CooldownMap> {
    public static final VariableRegistry<CooldownsVariable> VARIABLE_REGISTRY = new CooldownsVariableRegistry();

    public CooldownsVariable(String name, CooldownMap map) {
        super(name, map);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }

    @Override
    public String toString() {
        return getStored() == null ? "None" : "CooldownMap";
    }
}