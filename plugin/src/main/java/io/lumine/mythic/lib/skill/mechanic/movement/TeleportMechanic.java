package io.lumine.mythic.lib.skill.mechanic.movement;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.util.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.skill.targeter.LocationTargeter;
import io.lumine.mythic.lib.skill.targeter.location.DefaultLocationTargeter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class TeleportMechanic extends TargetMechanic {
    private final double yOffset;
    private final LocationTargeter targetLocation;

    public TeleportMechanic(ConfigObject config) {
        super(config);

        yOffset = config.getDouble("y_offset", 0);
        targetLocation = config.contains("target_location") ? MythicLib.plugin.getSkills().loadLocationTargeter(config.getObject("target_location")) : new DefaultLocationTargeter();
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        Location targetLocation = this.targetLocation.findTargets(meta).get(0);
        targetLocation.add(0, yOffset, 0);
        target.teleport(targetLocation);
    }
}
