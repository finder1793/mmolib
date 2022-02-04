package io.lumine.mythic.lib.comp.mythicmobs.mechanic;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.player.PlayerMetadata;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.logging.MythicLogger;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.damage.DamagingMechanic;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderDouble;
import io.lumine.xikage.mythicmobs.util.annotations.MythicMechanic;
import org.bukkit.entity.LivingEntity;

@MythicMechanic(
        author = "Indyuce",
        name = "mmodamage",
        aliases = {"mmod"},
        description = "Deals damage to the target (compatible with MMO plugins)"
)
public class MMODamageMechanic extends DamagingMechanic implements ITargetedEntitySkill {
    protected final PlaceholderDouble amount;

    /**
     * Can be empty if no damage type is registered.
     * <p>
     * It IS possible but any attack should be at least physical or magical.
     * It should also be either a weapon/skill/unarmed attack.
     */
    protected final DamageType[] types;

    public MMODamageMechanic(String line, MythicLineConfig config) {
        super(line, config);

        this.amount = PlaceholderDouble.of(config.getString(new String[]{"amount", "a"}, "1", new String[0]));
        String typesString = config.getString(new String[]{"type", "t"}, null, new String[0]);
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
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {

        if (target.isDead() || !(target.getBukkitEntity() instanceof LivingEntity) || data.getCaster().isUsingDamageSkill() || target.getHealth() <= 0)
            return false;

        double damage = amount.get(data, target) * data.getPower();

        PlayerMetadata caster = data.getVariables().has("MMOStatMap") ? (PlayerMetadata) data.getVariables().get("MMOStatMap").get()
                : MMOPlayerData.get(data.getCaster().getEntity().getUniqueId()).getStatMap().cache(EquipmentSlot.MAIN_HAND);
        AttackMetadata attackMeta = new AttackMetadata(new DamageMetadata(damage, types), caster);

        // Cooler damage types yeah
        MythicLib.plugin.getDamage().damage(attackMeta, (LivingEntity) target.getBukkitEntity(), !this.preventKnockback, this.preventImmunity);

        MythicLogger.debug(MythicLogger.DebugLevel.MECHANIC, "+ MMODamageMechanic fired for {0} with {1} power", new Object[]{damage, data.getPower()});
        return true;
    }
}
