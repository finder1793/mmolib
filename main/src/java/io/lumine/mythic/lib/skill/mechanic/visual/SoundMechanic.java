package io.lumine.mythic.lib.skill.mechanic.visual;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.util.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.mechanic.type.LocationMechanic;
import org.bukkit.Location;
import org.bukkit.Sound;

@MechanicMetadata
public class SoundMechanic extends LocationMechanic {
    private final Sound sound;
    private final float vol, pitch;

    public SoundMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("name");

        sound = Sound.valueOf(UtilityMethods.enumName(config.getString("name")));
        vol = (float) config.getDouble("volume", 1);
        pitch = (float) config.getDouble("pitch", 1);
    }

    @Override
    public void cast(SkillMetadata meta, Location loc) {
        loc.getWorld().playSound(loc, sound, vol, pitch);
    }
}