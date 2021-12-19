package io.lumine.mythic.lib.skill.variable.def;

import io.lumine.mythic.lib.api.stat.provider.StatProvider;
import io.lumine.mythic.lib.skill.variable.Variable;
import io.lumine.mythic.lib.skill.variable.VariableMetadata;
import io.lumine.mythic.lib.skill.variable.VariableRegistry;
import io.lumine.mythic.lib.skill.variable.def.registry.StatsVariableRegistry;

@VariableMetadata(name = "statMap")
public class StatsVariable extends Variable<StatProvider> {
    public static final VariableRegistry<StatsVariable> VARIABLE_REGISTRY = new StatsVariableRegistry();

    public StatsVariable(String name, StatProvider provider) {
        super(name, provider);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }

    @Override
    public String toString() {
        return getStored() == null ? "None" : "StatProvider";
    }
}