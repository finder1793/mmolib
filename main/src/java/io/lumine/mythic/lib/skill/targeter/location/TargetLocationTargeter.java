package io.lumine.mythic.lib.skill.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.targeter.LocationTargeter;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

public class TargetLocationTargeter implements LocationTargeter {

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(meta.getTargetLocation());
    }
}
