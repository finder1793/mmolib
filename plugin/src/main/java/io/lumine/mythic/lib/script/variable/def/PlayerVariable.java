package io.lumine.mythic.lib.script.variable.def;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.script.variable.SimpleVariableRegistry;
import io.lumine.mythic.lib.script.variable.Variable;
import io.lumine.mythic.lib.script.variable.VariableMetadata;
import io.lumine.mythic.lib.script.variable.VariableRegistry;
import org.bukkit.entity.Player;

@VariableMetadata(name = "player")
public class PlayerVariable extends Variable<Player> {
    public static final SimpleVariableRegistry<PlayerVariable> VARIABLE_REGISTRY = new SimpleVariableRegistry();

    static {

        // Inherited from entities
        VARIABLE_REGISTRY.registerVariable("id", var -> new IntegerVariable("temp", var.getStored().getEntityId()));
        VARIABLE_REGISTRY.registerVariable("uuid", var -> new StringVariable("temp", var.getStored().getUniqueId().toString()));
        VARIABLE_REGISTRY.registerVariable("type", var -> new StringVariable("temp", var.getStored().getType().name()));
        VARIABLE_REGISTRY.registerVariable("location", var -> new PositionVariable("temp", var.getStored().getLocation()));
        VARIABLE_REGISTRY.registerVariable("health", var -> new DoubleVariable("temp", var.getStored().getHealth()));
        VARIABLE_REGISTRY.registerVariable("looking", var -> new PositionVariable("temp", var.getStored().getWorld(), var.getStored().getEyeLocation().getDirection()));
        VARIABLE_REGISTRY.registerVariable("velocity", var -> new PositionVariable("temp", var.getStored().getWorld(), var.getStored().getVelocity()));
        VARIABLE_REGISTRY.registerVariable("height", var -> new DoubleVariable("temp", var.getStored().getHeight()));
        VARIABLE_REGISTRY.registerVariable("attribute", var -> new AttributesVariable("temp", var.getStored()));
        VARIABLE_REGISTRY.registerVariable("fire_ticks", var -> new IntegerVariable("temp", var.getStored().getFireTicks()));

        // Player specific
        VARIABLE_REGISTRY.registerVariable("stat", var -> new StatsVariable("temp", MMOPlayerData.get(var.getStored()).getStatMap()));
        VARIABLE_REGISTRY.registerVariable("cooldown", var -> new CooldownsVariable("temp", MMOPlayerData.get(var.getStored()).getCooldownMap()));
        VARIABLE_REGISTRY.registerVariable("name", var -> new StringVariable("temp", var.getStored().getName()));
    }

    public PlayerVariable(String name, Player player) {
        super(name, player);
    }

    @Override
    public VariableRegistry getVariableRegistry() {
        return VARIABLE_REGISTRY;
    }

    @Override
    public String toString() {
        return getStored() == null ? "None" : getStored().getName();
    }
}