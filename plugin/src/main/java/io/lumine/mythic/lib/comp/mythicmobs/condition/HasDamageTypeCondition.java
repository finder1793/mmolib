package io.lumine.mythic.lib.comp.mythicmobs.condition;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.conditions.ISkillMetaCondition;
import io.lumine.mythic.core.skills.SkillCondition;
import io.lumine.mythic.core.utils.annotations.MythicCondition;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.result.MythicMobsSkillResult;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@MythicCondition(author = "Gunging", name = "mmodamagetype", aliases = {}, description = "If the skill is of this damage type. ")
public class HasDamageTypeCondition extends SkillCondition implements ISkillMetaCondition {
    protected final boolean exact;
    protected final Set<DamageType> types = new HashSet<>();

    public HasDamageTypeCondition(@NotNull MythicLineConfig mlc) {
        super(mlc.getLine());

        // Require exact damage types
        this.exact = mlc.getBoolean("exact", false);

        // Read types being sought
        String typesString = mlc.getString(new String[]{"type", "t", "types"}, null);
        if (typesString != null && !typesString.isEmpty() && !typesString.equalsIgnoreCase("NONE"))
            for (String str : typesString.replace("<&cm>", ",").split(","))
                this.types.add(DamageType.valueOf(UtilityMethods.enumName(str)));
    }

    @Override
    public boolean check(SkillMetadata skillMetadata) {

        /*
         * Making it a Validate statement would cause MythicMob skills to crash
         * and stop execution if the condition was used through a means that
         * produced no attack meta, where at least I personally would use this
         * very condition to check if the skill had no damage meta at all.
         */

        Set<DamageType> attackDamageTypes;

        // Skill has no damage types
        if (!skillMetadata.getVariables().has(MythicMobsSkillResult.MMOSKILL_VAR_ATTACK)) {

            // Empty set
            attackDamageTypes = new HashSet<>();

        // Skill does have damage types
        } else {

            // Retrieve Attack Meta
            AttackMetadata attackMeta = (AttackMetadata) skillMetadata.getVariables().get(MythicMobsSkillResult.MMOSKILL_VAR_ATTACK).get();
            Validate.isTrue(!attackMeta.hasExpired(), "Attack meta has expired");

            // Read all damage types of this attack
            attackDamageTypes = attackMeta.getDamage().collectTypes();
        }


        // Exact match
        if (exact)
            return attackDamageTypes.equals(this.types);

        // Must contain at least one
        for (DamageType damageType : attackDamageTypes)
            if (this.types.contains(damageType))
                return true;

        return false;
    }
}
