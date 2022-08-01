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
    private final boolean stack;

    public SetOnFireMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("ticks");

        stack = config.getBoolean("stack", false);
        ticks = config.getDoubleFormula("ticks");
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        final int ticks = (int) this.ticks.evaluate(meta);
        target.setFireTicks(ticks + (stack ? target.getFireTicks() : 0));
    }
}
