package io.lumine.mythic.lib.skill.custom.mechanic.offense;

import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.entity.Entity;

@MechanicMetadata
public class AdditiveDamageBuffMechanic extends TargetMechanic {
    private final DoubleFormula amount;
    private final DamageType damageType;

    public AdditiveDamageBuffMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("amount");

        amount = new DoubleFormula(config.getString("amount"));
        damageType = config.contains("damage_type") ? DamageType.valueOf(config.getString("damage_type").toUpperCase()) : null;
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        if (damageType == null)
            meta.getAttack().getDamage().additiveModifier(amount.evaluate(meta));
        else
            meta.getAttack().getDamage().additiveModifier(amount.evaluate(meta), damageType);
    }
}
