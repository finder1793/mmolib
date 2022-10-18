package io.lumine.mythic.lib.api.stat.provider;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * This class is used to generalize stat maps to both players and non-player
 * entities. Some statistics should apply even if the target entity is NOT
 * a player (in that case, you can't just get the MMOPlayerData and then
 * the stat map).
 * <p>
 * See {@link EntityStatProvider} for more info
 *
 * @author indyuce
 */
public interface StatProvider {
    double getStat(String stat);

    static StatProvider get(LivingEntity living) {
        return isRealPlayer(living) ? MMOPlayerData.get(living.getUniqueId()).getStatMap() : new EntityStatProvider(living);
    }

    static StatProvider generate(LivingEntity living, EquipmentSlot actionHand) {
        return isRealPlayer(living) ? MMOPlayerData.get(living.getUniqueId()).getStatMap().cache(actionHand) : new EntityStatProvider(living);
    }

    private static boolean isRealPlayer(Object entity) {
        return entity instanceof Player && !((Player) entity).hasMetadata("NPC");
    }
}
