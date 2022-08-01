package io.lumine.mythic.lib.script.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.targeter.LocationTargeter;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

public class TargetLocationTargeter extends LocationTargeter {
    public TargetLocationTargeter() {
        super(false);
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(meta.getTargetLocation());
    }
}
