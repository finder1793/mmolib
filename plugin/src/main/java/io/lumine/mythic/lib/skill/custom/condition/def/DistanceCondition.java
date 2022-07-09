package io.lumine.mythic.lib.skill.custom.condition.def;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.condition.Condition;
import io.lumine.mythic.lib.skill.custom.targeter.LocationTargeter;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;

import java.util.Optional;

public class DistanceCondition extends Condition {
    private final LocationTargeter targeter;
    private final double distanceMax;

    public DistanceCondition(ConfigObject config) {
        super(config);

        config.validateKeys("location", "max");

        targeter = MythicLib.plugin.getSkills().loadLocationTargeter(config.getObject("location"));
        distanceMax = config.getDouble("max");
    }

    @Override
    public boolean isMet(SkillMetadata meta) {
        Location loc = meta.getSkillLocation(false);
        Optional<Location> target = targeter.findTargets(meta).stream().findAny();
        Validate.isTrue(target.isPresent());
        return target.get().distance(loc) <= distanceMax;
    }
}
