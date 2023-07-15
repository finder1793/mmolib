package io.lumine.mythic.lib.script.mechanic.player;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class SudoMechanic extends TargetMechanic {
    private final String message;

    public SudoMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("format");

        message = config.getString("format");
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        Bukkit.dispatchCommand(target, meta.parseString(message));
    }
}
