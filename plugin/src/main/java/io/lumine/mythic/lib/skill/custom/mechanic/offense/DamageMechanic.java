package io.lumine.mythic.lib.skill.custom.mechanic.offense;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.skill.custom.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashSet;
import java.util.Set;

@MechanicMetadata
public class DamageMechanic extends TargetMechanic {
    private final DoubleFormula amount;
    private final boolean knockback, ignoreImmunity;
    private final DamageType[] types;

    public DamageMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("amount");

        amount = new DoubleFormula(config.getString("amount"));
        knockback = config.getBoolean("knockback", true);
        ignoreImmunity = config.getBoolean("ignore_immunity", false);

        // Look for damage type
        Set<DamageType> damageTypes = new HashSet<>();
        if (config.contains("damage_type"))
            for (String typeFormat : config.getString("damage_type").split("\\,"))
                damageTypes.add(DamageType.valueOf(typeFormat.toUpperCase()));

            // By default, magical-skill damage
        else {
            damageTypes.add(DamageType.MAGIC);
            damageTypes.add(DamageType.SKILL);
        }

        types = damageTypes.toArray(new DamageType[0]);
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        Validate.isTrue(target instanceof LivingEntity, "Cannot damage a non living entity");

        // This ignores the 'knockback' and 'ignore-immunity' options
        if (meta.hasAttackBound()) {
            meta.getAttack().getDamage().add(amount.evaluate(meta), types);
            return;
        }

        AttackMetadata result = new AttackMetadata(new DamageMetadata(amount.evaluate(meta), types), meta.getCaster());
        MythicLib.plugin.getDamage().damage(result, (LivingEntity) target, knockback, ignoreImmunity);
    }
}
