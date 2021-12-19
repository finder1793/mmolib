package io.lumine.mythic.lib.skill.targeter;

import io.lumine.mythic.lib.skill.SkillMetadata;
import org.bukkit.Location;

import java.util.List;

@FunctionalInterface
public interface LocationTargeter {

    public List<Location> findTargets(SkillMetadata meta);
}
