package io.lumine.mythic.lib.script.variable.def;

import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.script.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.script.variable.VariableMetadata;
import io.lumine.mythic.lib.script.variable.VariableRegistry;

@VariableMetadata(name = "attackMeta")
public class AttackMetadataVariable extends Variable<AttackMetadata> {
    public static final SimpleVariableRegistry<AttackMetadata> VARIABLE_REGISTRY = new SimpleVariableRegistry<>();

    static {
        VARIABLE_REGISTRY.registerVariable("damage", var -> new DoubleVariable("temp", var.getDamage().getDamage()));
    }

    public AttackMetadataVariable(String name, AttackMetadata attackMetadata) {
        super(name, attackMetadata);
    }

    @Override
    public VariableRegistry<Variable<AttackMetadata>> getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }

    @Override
    public String toString() {
        return getStored() == null ? "None" : "AttackMeta";
    }
}