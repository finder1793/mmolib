package io.lumine.mythic.lib.comp.mythicmobs.mechanic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.damage.DamagingMechanic;
import io.lumine.mythic.core.utils.annotations.MythicMechanic;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.player.PlayerMetadata;
import io.lumine.mythic.lib.skill.result.MythicMobsSkillResult;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@MythicMechanic(
        author = "Indyuce",
        name = "mmodamage",
        aliases = {"mmod"},
        description = "Deals damage to the target (compatible with MMO plugins)"
)
public class MMODamageMechanic extends DamagingMechanic implements ITargetedEntitySkill {
    protected final PlaceholderDouble amount;
    protected final boolean ignoreMMOAttack;

    /**
     * Can be empty if no damage type is registered.
     * <p>
     * It IS possible but any attack should be at least physical or magical.
     * It should also be either a weapon/skill/unarmed attack.
     */
    protected final DamageType[] types;

    public MMODamageMechanic(SkillExecutor manager, String line, MythicLineConfig config) {
        super(manager, line, config);

        this.amount = PlaceholderDouble.of(config.getString(new String[]{"amount", "a"}, "1", new String[0]));
        String typesString = config.getString(new String[]{"type", "t", "types"}, null, new String[0]);
        this.ignoreMMOAttack = config.getBoolean(new String[]{"ignoreMMOAttack", "immo"}, false);
        this.types = (typesString == null || typesString.isEmpty() || "NONE".equalsIgnoreCase(typesString)) ? new DamageType[0] : toDamageTypeArray(typesString);
    }

    private DamageType[] toDamageTypeArray(String typesString) {
        String[] split = typesString.split("\\,");
        DamageType[] array = new DamageType[split.length];

        for (int i = 0; i < array.length; i++)
            array[i] = DamageType.valueOf(UtilityMethods.enumName(split[i]));

        return array;
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {

        if (target.isDead() || !(target.getBukkitEntity() instanceof LivingEntity) || data.getCaster().isUsingDamageSkill() || target.getHealth() <= 0)
            return SkillResult.INVALID_TARGET;

        // Calculate damage
        double damage = amount.get(data, target) * data.getPower();

        /*
         * If the caster is not a player, this method falls off to the normal
         * DamageMechanic of vanilla mythicmobs, rather than generating a null
         * pointer exception when trying to find the PlayerData of this entity.
         */
        if (!(data.getCaster().getEntity().getBukkitEntity() instanceof Player)) {

            // Deals damage like normal mythicmobs hit each other
            MythicLib.plugin.getDamage().registerCustomDamage(new DamageMetadata(damage, types), target.getBukkitEntity());
            this.doDamage(data.getCaster(), target, damage);
            return SkillResult.SUCCESS;
        }

        if (!ignoreMMOAttack && data.getVariables().has(MythicMobsSkillResult.MMOSKILL_VAR_ATTACK)) {
            AttackMetadata currentAttack = (AttackMetadata) data.getVariables().get(MythicMobsSkillResult.MMOSKILL_VAR_ATTACK).get();
            if (!currentAttack.hasExpired()) {
                currentAttack.getDamage().add(damage, types);
                return SkillResult.SUCCESS;
            }
        }

        PlayerMetadata caster = data.getVariables().has(MythicMobsSkillResult.MMOSKILL_VAR_STATS) ? (PlayerMetadata) data.getVariables().get(MythicMobsSkillResult.MMOSKILL_VAR_STATS).get()
                : MMOPlayerData.get(data.getCaster().getEntity().getUniqueId()).getStatMap().cache(EquipmentSlot.MAIN_HAND);
        MythicLib.plugin.getDamage().damage(new AttackMetadata(new DamageMetadata(damage, types), caster), (LivingEntity) target.getBukkitEntity(), !this.preventKnockback, this.preventImmunity);
        return SkillResult.SUCCESS;
    }
}
