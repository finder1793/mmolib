package io.lumine.mythic.lib.comp;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.AttackHandler;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class McMMOAttackHandler implements AttackHandler {
    private static final String METADATA_KEY = "mcMMO: Custom Damage";

    @Override
    public AttackMetadata getAttack(EntityDamageEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!(event instanceof EntityDamageByEntityEvent) || !entity.hasMetadata(METADATA_KEY))
            return null;

        EntityDamageByEntityEvent event1 = (EntityDamageByEntityEvent) event;
        if (!(event1.getDamager() instanceof Player) || !MMOPlayerData.has(event1.getDamager().getUniqueId()))
            return null;

        return new AttackMetadata(new DamageMetadata(), (LivingEntity) entity, MMOPlayerData.get(event1.getDamager().getUniqueId()).getStatMap().cache(EquipmentSlot.MAIN_HAND));
    }
}
