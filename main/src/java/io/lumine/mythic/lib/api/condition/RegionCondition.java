package io.lumine.mythic.lib.api.condition;

import io.lumine.mythic.lib.api.MMOLineConfig;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Needs a big rework
 */
public class RegionCondition extends MMOCondition implements LocationCondition {
    private final String region;

    public RegionCondition(MMOLineConfig config) {
        super(config);

        config.validate("name");
        this.region = config.getString("name");
    }

    @Override
    public boolean check(Location location) {
        List<String> regions = new ArrayList<>();
        WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery()
                .getApplicableRegions(BukkitAdapter.adapt(location)).getRegions().forEach(region -> regions.add(region.getId()));
        return regions.contains(region);
    }
}
