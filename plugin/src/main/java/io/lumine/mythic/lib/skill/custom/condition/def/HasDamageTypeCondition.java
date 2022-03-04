package io.lumine.mythic.lib.skill.custom.condition.def;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.condition.Condition;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if the attackMeta bound to the skillMeta has some damage type.
 * This condition can only be used when using a trigger type like DAMAGED or DAMAGE
 * <p>
 * This checks if the attack has at least one of the damage types provided.
 */
public class HasDamageTypeCondition extends Condition {
    private final List<DamageType> types = new ArrayList<>();

    public HasDamageTypeCondition(ConfigObject config) {
        super(config);

        config.validateKeys("types");

        for (String str : config.getString("types").split("\\,"))
            types.add(DamageType.valueOf(UtilityMethods.enumName(str)));
    }

    @Override
    public boolean isMet(SkillMetadata meta) {
        for (DamageType checked : types)
            if (meta.getAttack().getDamage().hasType(checked))
                return true;

        return false;
    }
}