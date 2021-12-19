package io.lumine.mythic.lib.skill.mechanic.offense;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.util.ConfigObject;
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
    private final Set<DamageType> types = new HashSet<>();

    public DamageMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("amount");

        amount = new DoubleFormula(config.getString("amount"));
        knockback = config.getBoolean("knockback", true);
        ignoreImmunity = config.getBoolean("ignore_immunity", false);

        // Look for damage type
        if (config.contains("damage_type"))
            for (String typeFormat : config.getString("damage_type").split("\\,"))
                types.add(DamageType.valueOf(typeFormat.toUpperCase()));

            // By default, magical-skill damage
        else {
            types.add(DamageType.MAGIC);
            types.add(DamageType.SKILL);
        }
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        Validate.isTrue(target instanceof LivingEntity, "Cannot damage a non living entity");

        AttackMetadata result = new AttackMetadata(new DamageMetadata(amount.evaluate(meta), types.toArray(new DamageType[0])), meta.getStats());
        MythicLib.plugin.getDamage().damage(result, (LivingEntity) target, knockback, ignoreImmunity);
    }
}
