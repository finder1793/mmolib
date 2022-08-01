package io.lumine.mythic.lib.script.condition.location;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.script.condition.type.LocationCondition;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.HashSet;
import java.util.Set;

/**
 * Checks for the caster's current biome
 */
public class BiomeCondition extends LocationCondition {
    private final Set<Biome> biomes = new HashSet<>();

    public BiomeCondition(ConfigObject config) {
        super(config, true);

        config.validateKeys("name");
        for (String str : config.getString("name").split(","))
            biomes.add(Biome.valueOf(UtilityMethods.enumName(str)));
    }

    @Override
    public boolean isMet(SkillMetadata meta, Location loc) {
        return biomes.contains(loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }
}