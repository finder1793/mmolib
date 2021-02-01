package io.lumine.mythic.lib.api.condition;


import io.lumine.mythic.lib.api.MMOLineConfig;
import org.bukkit.World;

public class WeatherCondition extends MMOCondition implements WorldCondition {
    public WeatherCondition(MMOLineConfig config) {
        super(config);
    }

    @Override
    public boolean check(World world) {
        return world.hasStorm();
    }
}
