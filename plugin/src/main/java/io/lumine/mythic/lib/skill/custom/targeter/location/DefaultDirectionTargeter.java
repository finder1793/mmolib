package io.lumine.mythic.lib.skill.custom.targeter.location;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.targeter.LocationTargeter;
import io.lumine.mythic.lib.skill.handler.def.simple.Empowered_Attack;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.configobject.EmptyConfigObject;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Takes one block in front of the caster's eyes location
 */
public class DefaultDirectionTargeter extends LocationTargeter {
    public DefaultDirectionTargeter() {
        super(false);
    }

    @Override
    public List<Location> findTargets(SkillMetadata meta) {
        Location loc = meta.getCaster().getPlayer().getEyeLocation();
        return Arrays.asList(loc.add(loc.getDirection()));
    }
}
