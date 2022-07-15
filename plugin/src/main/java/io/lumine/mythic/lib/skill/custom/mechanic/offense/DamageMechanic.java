package io.lumine.mythic.lib.skill.custom.mechanic.offense;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.element.Element;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

@MechanicMetadata
public class DamageMechanic extends TargetMechanic {
    private final DoubleFormula amount;
    private final boolean knockback, ignoreImmunity;
    private final DamageType[] types;
    private final Element element;

    public DamageMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("amount");

        amount = new DoubleFormula(config.getString("amount"));
        knockback = config.getBoolean("knockback", true);
        ignoreImmunity = config.getBoolean("ignore_immunity", false);

        // Look for damage type
        if (config.contains("damage_type")) {
            final String[] split = config.getString("damage_type").split("\\,");
            types = new DamageType[split.length];
            for (int i = 0; i < split.length; i++)
                types[i] = DamageType.valueOf(split[i].toLowerCase());
        }

        // By default, magical-skill damage
        else
            types = new DamageType[]{DamageType.MAGIC, DamageType.SKILL};

        // Elemental attack?
        element = config.contains("element") ? MythicLib.plugin.getElements().get(config.getString("element")) : null;
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        Validate.isTrue(target instanceof LivingEntity, "Cannot damage a non living entity");

        // This ignores the 'knockback' and 'ignore-immunity' options
        if (meta.hasAttackBound() && meta.getAttack().getTarget().equals(target)) {
            if (element == null)
                meta.getAttack().getDamage().add(amount.evaluate(meta), types);
            else
                meta.getAttack().getDamage().add(amount.evaluate(meta), element, types);
            return;
        }

        AttackMetadata result = new AttackMetadata(new DamageMetadata(amount.evaluate(meta), types), (LivingEntity) target, meta.getCaster());
        MythicLib.plugin.getDamage().registerAttack(result, knockback, ignoreImmunity);
    }
}
