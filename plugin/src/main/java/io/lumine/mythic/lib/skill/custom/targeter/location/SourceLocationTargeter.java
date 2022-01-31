package io.lumine.mythic.lib.skill.custom.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.targeter.LocationTargeter;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.configobject.EmptyConfigObject;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

public class SourceLocationTargeter extends LocationTargeter {
    public SourceLocationTargeter() {
        super(false);
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        return Arrays.asList(meta.getSourceLocation());
    }
}
