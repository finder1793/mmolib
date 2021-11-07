package io.lumine.mythic.lib.comp.mythicmobs;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.logging.MythicLogger;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.damage.DamagingMechanic;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderDouble;
import io.lumine.xikage.mythicmobs.util.annotations.MythicMechanic;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;
import java.util.stream.Collectors;

@MythicMechanic(
        author = "Indyuce",
        name = "mmodamage",
        aliases = {"mmod"},
        description = "Deals damage to the target (compatible with MMO)"
)
public class MMODamageMechanic extends DamagingMechanic implements ITargetedEntitySkill {
    protected final PlaceholderDouble amount;
    protected final DamageType[] types;

    public MMODamageMechanic(String line, MythicLineConfig config) {
        super(line, config);

        this.amount = PlaceholderDouble.of(config.getString(new String[]{"amount", "a"}, "1", new String[0]));
        String[] typesFormat = config.getString(new String[]{"type", "t"}, "SKILL,MAGIC", new String[0]).split("\\,");
        this.types = Arrays.asList(typesFormat).stream().map(str -> DamageType.valueOf(str.toUpperCase())).collect(Collectors.toList()).toArray(new DamageType[0]);
    }

    @Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {

        if (!target.isDead() && target.getBukkitEntity() instanceof LivingEntity && !data.getCaster().isUsingDamageSkill() && (!target.isLiving() || !(target.getHealth() <= 0.0D))) {

            double damage = amount.get(data, target) * data.getPower();

            StatMap.CachedStatMap statMap = data.getVariables().has("MMOStatMap") ? (StatMap.CachedStatMap) data.getVariables().get("MMOStatMap").get()
                    : MMOPlayerData.get(data.getCaster().getEntity().getUniqueId()).getStatMap().cache(EquipmentSlot.MAIN_HAND);
            AttackMetadata attackMeta = new AttackMetadata(new DamageMetadata(damage, types), statMap);

            // Cooler damage types yeah
            MythicLib.plugin.getDamage().damage(attackMeta, (LivingEntity) target.getBukkitEntity(), !this.preventKnockback, this.preventImmunity);

            MythicLogger.debug(MythicLogger.DebugLevel.MECHANIC, "+ MMODamageMechanic fired for {0} with {1} power", new Object[]{damage, data.getPower()});
            return true;
        } else {
            return false;
        }
    }
}
