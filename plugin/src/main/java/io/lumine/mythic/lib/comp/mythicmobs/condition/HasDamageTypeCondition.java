package io.lumine.mythic.lib.comp.mythicmobs.condition;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.conditions.ISkillMetaCondition;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.core.skills.SkillCondition;
import io.lumine.mythic.core.skills.conditions.ConditionAction;
import io.lumine.mythic.core.utils.annotations.MythicCondition;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager;
import io.lumine.mythic.lib.comp.mythicmobs.mechanic.MMODamageMechanic;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamagePacket;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.result.MythicMobsSkillResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@MythicCondition(author = "Gunging", name = "mmoDamageType", aliases = {}, description = "If the skill is of this damage type. ")
public class HasDamageTypeCondition extends SkillCondition implements ISkillMetaCondition {

    boolean exact;
    protected final DamageType[] types;

    public HasDamageTypeCondition(@NotNull MythicLineConfig mlc) {
        super(mlc.getLine());

        //region Fix alternative condition actions
        String action = mlc.getString(new String[] {"action", "a"}, "REQUIRED");
        action = action.replace("<&sp>", " ");
        if (action.startsWith("\"")) { action = action.substring(1); }
        if (action.endsWith("\"")) { action = action.substring(0, action.length() - 1); }
        this.ACTION = ConditionAction.REQUIRED;
        this.actionVar = null;
        String[] split = action.split(" ");
        if (split.length >= 1) {
            if (ConditionAction.isAction(split[0])) {
                this.ACTION = ConditionAction.valueOf(split[0].toUpperCase());
                if (split.length > 1) {
                    this.actionVar = PlaceholderString.of(split[1]); }
            } else {
                this.conditionVar = split[0];
                if (split.length > 1 && ConditionAction.isAction(split[1])) {
                    this.ACTION = ConditionAction.valueOf(split[1].toUpperCase());
                    if (split.length > 2) { this.actionVar = PlaceholderString.of(split[2]); } } } }
        //endregion

        this.exact = mlc.getBoolean("exact", false);

        // Read types being seeked
        String typesString = mlc.getString(new String[]{"type", "t", "types"}, null, new String[0]);
        this.types = (typesString == null || typesString.isEmpty() || "NONE".equalsIgnoreCase(typesString)) ? new DamageType[0] : toDamageTypeArray(typesString);
    }

    DamageType[] toDamageTypeArray(String typesString) {
        String[] split = typesString.replace("<&cm>", ",").split("\\,");
        ArrayList<DamageType> types = new ArrayList<>();

        for (String s : split) {
            try {
                DamageType t = DamageType.valueOf(UtilityMethods.enumName(s));
                types.add(t);

            } catch (IllegalArgumentException ignored) { } }

        // To Array
        DamageType[] l = new DamageType[types.size()];
        return types.toArray(l);
    }

    @Override
    public boolean check(SkillMetadata skillMetadata) {
        /*DTC*/MythicCraftingManager.log("\u00a78DTC\u00a73 ST\u00a77 Searching for types\u00a7f x" + types.length);

        // Missing?
        if (!skillMetadata.getVariables().has(MythicMobsSkillResult.MMOSKILL_VAR_ATTACK)) {
            /*DTC*/MythicCraftingManager.log("\u00a78DTC\u00a73 ST\u00a77 Skill had no damage meta\u00a7e " + (types.length == 0));

            /*
             * Must have no required damage types to succeed.
             *
             * Exact or not, if the skill metadata has no damage types, this
             * condition must be checking for 'NONE' type to make sense.
             */
            return types.length == 0;
        }

        // All right let us check the skill metadata
        AttackMetadata attack = (AttackMetadata) skillMetadata.getVariables().get(MythicMobsSkillResult.MMOSKILL_VAR_ATTACK).get();
        /*DTC*/MythicCraftingManager.log("\u00a78DTC\u00a73 ST\u00a77 Meta\u00a7f " + attack.getDamage().toString());

        // Read all damage types of this attack
        ArrayList<DamageType> therein = new ArrayList<>();
        for (DamagePacket packet : attack.getDamage().getPackets()) {

            //  For every damage type
            for (DamageType type : packet.getTypes()) {

                // Include type
                if (!therein.contains(type)) { therein.add(type); }
            }
        }

        // Must have all types
        for (DamageType required : types) {
            boolean contained = false;

            // Compare to those in there
            for (DamageType observed : therein) {

                // Was it contained?
                if (required.equals(observed)) {
                    contained = true;
                    break;
                }
            }

            // Not contained? That's a failure
            if (!contained) {
                /*DTC*/MythicCraftingManager.log("\u00a78DTC\u00a73 ST\u00a77 Not contained\u00a7c " + required.toString());
                return false; }
        }

        // All the required types were contained, if not exact, we are done here
        if (!exact) {
            /*DTC*/MythicCraftingManager.log("\u00a78DTC\u00a73 ST\u00a7a All Types Contained");
            return true; }

        // Must require all types
        for (DamageType observed : therein) {
            boolean contained = false;

            // Compare to those expected
            for (DamageType required : types) {

                // Was it contained?
                if (required.equals(observed)) {
                    contained = true;
                    break;
                }
            }

            // Not contained? That's a failure
            if (!contained) {
                /*DTC*/MythicCraftingManager.log("\u00a78DTC\u00a73 ST\u00a77 Not expected\u00a7c " + observed.toString());
                return false; }
        }

        // Success
        /*DTC*/MythicCraftingManager.log("\u00a78DTC\u00a73 ST\u00a7a Exact Types Matched");
        return true;
    }
}
