package io.lumine.mythic.lib.api.condition;

import io.lumine.mythic.lib.api.MMOLineConfig;
import org.bukkit.Location;

public class BiomeCondition extends MMOCondition implements LocationCondition {
    private final String biome;

    public BiomeCondition(MMOLineConfig config) {
        super(config);

        config.validate("biome");
        this.biome = config.getString("biome");
    }

    @Override
    public boolean check(Location location) {
        return location.getWorld().getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ()).name().equalsIgnoreCase(biome);
    }
}
