package io.lumine.mythic.lib.script.mechanic.offense;

import io.lumine.mythic.lib.script.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.script.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.entity.Entity;

@MechanicMetadata
public class SetOnFireMechanic extends TargetMechanic {
    private final DoubleFormula ticks;
    private final boolean stack, min, max;

    public SetOnFireMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("ticks");

        stack = config.getBoolean("stack", false);
        min = config.getBoolean("min", false);
        max = config.getBoolean("max", false);
        ticks = config.getDoubleFormula("ticks");
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        int ticks = (int) this.ticks.evaluate(meta);
        if (stack) ticks += target.getFireTicks();
        else if (max) ticks = Math.max(ticks, target.getFireTicks());
        else if (min) ticks = Math.min(ticks, target.getFireTicks());

        target.setFireTicks(ticks);
    }
}
