package io.lumine.mythic.lib.script.mechanic.visual;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.script.mechanic.type.LocationMechanic;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;
import org.bukkit.Sound;

@MechanicMetadata
public class SoundMechanic extends LocationMechanic {
    private final Sound sound;
    private final float vol, pitch;

    public SoundMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("sound");

        sound = Sound.valueOf(UtilityMethods.enumName(config.getString("sound")));
        vol = (float) config.getDouble("volume", 1);
        pitch = (float) config.getDouble("pitch", 1);
    }

    @Override
    public void cast(SkillMetadata meta, Location loc) {
        loc.getWorld().playSound(loc, sound, vol, pitch);
    }
}