package io.lumine.mythic.lib.comp.mythicmobs;

import io.lumine.mythic.lib.damage.AttackHandler;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import org.bukkit.entity.Entity;

import java.util.Optional;

/**
 * @deprecated This is only used when using the default MythicMobs damage
 *         mechanic, which is fine but it DOES NOT correctly apply stats like 'Magic
 *         Damage' or anything else related to damage types.
 *         <p>
 *         As you can see in {@link #getAttack(Entity)} there is no way to retrieve the
 *         damage type from a {@link io.lumine.xikage.mythicmobs.skills.damage.DamageMetadata} instance
 *         <p>
 *         Also there is no way of retrieving cached player stats because you can't
 *         get a SkillMetadata from a DamageMetadata
 */
@Deprecated
public class MythicMobsAttackHandler implements AttackHandler {

    @Override
    public AttackMetadata getAttack(Entity entity) {
        io.lumine.xikage.mythicmobs.skills.damage.DamageMetadata metadata = (io.lumine.xikage.mythicmobs.skills.damage.DamageMetadata) BukkitAdapter.adapt(entity).getMetadata("skill-damage").get();
        DamageMetadata result = new DamageMetadata(metadata.getAmount(), DamageType.MAGIC, DamageType.SKILL);
        return new AttackMetadata(result, MMOPlayerData.get(metadata.getDamager().getEntity().getUniqueId()).getStatMap().cache(EquipmentSlot.MAIN_HAND));
    }

    @Override
    public boolean isAttacked(Entity entity) {
        Optional<Object> opt = BukkitAdapter.adapt(entity).getMetadata("skill-damage");
        if (!opt.isPresent())
            return false;

        io.lumine.xikage.mythicmobs.skills.damage.DamageMetadata metadata = (io.lumine.xikage.mythicmobs.skills.damage.DamageMetadata) opt.get();
        return metadata.getDamager().getEntity() instanceof AbstractPlayer;
    }
}
