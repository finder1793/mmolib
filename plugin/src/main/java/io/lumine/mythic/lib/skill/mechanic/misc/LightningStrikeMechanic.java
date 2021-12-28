package io.lumine.mythic.lib.skill.mechanic.misc;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.mechanic.type.LocationMechanic;
import org.bukkit.Location;

@MechanicMetadata
public class LightningStrikeMechanic extends LocationMechanic {

    /**
     * Whether or not it actually deals damage and causes fire
     */
    private final boolean effect;

    public LightningStrikeMechanic(ConfigObject config) {
        super(config);

        effect = config.getBoolean("effect", false);
    }

    @Override
    public void cast(SkillMetadata meta, Location loc) {
        if (effect)
            loc.getWorld().strikeLightningEffect(loc);
        else
            loc.getWorld().strikeLightning(loc);
    }
}