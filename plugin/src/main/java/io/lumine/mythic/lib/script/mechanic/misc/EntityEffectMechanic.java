package io.lumine.mythic.lib.script.mechanic.misc;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.script.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.script.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;

@MechanicMetadata
public class EntityEffectMechanic extends TargetMechanic {
    private final EntityEffect effect;

    public EntityEffectMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("effect");

        effect = EntityEffect.valueOf(UtilityMethods.enumName(config.getString("effect")));
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        target.playEffect(effect);
    }
}
