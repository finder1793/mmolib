package io.lumine.mythic.lib.script.mechanic.visual;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.script.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.script.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.lang3.Validate;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;

@MechanicMetadata
public class PlayerSoundMechanic extends TargetMechanic {
    private final Sound sound;
    private final String soundString;
    private final DoubleFormula vol, pitch;

    public PlayerSoundMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("sound");

        soundString = config.getString("sound");
        //The string given as input might not correspond to an element of the enum (custom sound)
        sound = Arrays.stream(Sound.values()).map(Sound::name).anyMatch(name -> UtilityMethods.enumName(soundString).equals(name)) ?
                Sound.valueOf(UtilityMethods.enumName(soundString)) : null;
        vol = config.getDoubleFormula("volume", DoubleFormula.constant(1));
        pitch = config.getDoubleFormula("pitch", DoubleFormula.constant(1));
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        Validate.isTrue(target instanceof Player, "Target is not player");

        final Player player = (Player) target;
        final float vol = (float) this.vol.evaluate(meta);
        final float pitch = (float) this.pitch.evaluate(meta);

        if (sound == null) player.playSound(target.getLocation(), soundString, vol, pitch);
        else player.playSound(target.getLocation(), sound, vol, pitch);
    }
}