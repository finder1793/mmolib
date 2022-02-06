package io.lumine.mythic.lib.skill.custom.mechanic.misc;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.Mechanic;
import io.lumine.mythic.lib.skill.custom.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Bukkit;

@MechanicMetadata
public class DispatchCommandMechanic extends Mechanic {
    private final String message;

    public DispatchCommandMechanic(ConfigObject config) {
        config.validateKeys("format");

        message = config.getString("format");
    }

    @Override
    public void cast(SkillMetadata meta) {

        // Dispatch command
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), meta.parseString(message));
    }
}
