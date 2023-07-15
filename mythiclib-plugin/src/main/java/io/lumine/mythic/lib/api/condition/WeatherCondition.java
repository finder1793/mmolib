package io.lumine.mythic.lib.api.condition;


import io.lumine.mythic.lib.api.MMOLineConfig;
import io.lumine.mythic.lib.api.condition.type.MMOCondition;
import org.bukkit.World;

public class WeatherCondition extends MMOCondition implements io.lumine.mythic.lib.api.condition.type.WorldCondition {
    public WeatherCondition(MMOLineConfig config) {
        super(config);
    }

    @Override
    public boolean check(World world) {
        return world.hasStorm();
    }
}
