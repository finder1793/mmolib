package io.lumine.mythic.lib.script.mechanic.misc;

import io.lumine.mythic.lib.script.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

@MechanicMetadata
public class DispatchCommandMechanic extends TargetMechanic {
    private final String message;
    private final boolean fromConsole;

    public DispatchCommandMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("format");

        message = config.getString("format");
        fromConsole = config.getBoolean("from_console", true);
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        // Dispatch command

        Bukkit.dispatchCommand(fromConsole ? Bukkit.getConsoleSender() : target, meta.parseString(message));

    }
}
