package io.lumine.mythic.lib.comp.interaction;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@Deprecated
public interface TargetRestriction {

    /**
     * Called whenever a player tries to damage OR buff an entity.
     * <p>
     * This should be used by:
     * - plugins which implement friendly fire player sets like parties, guilds, nations, factions....
     * - plugins which implement custom invulnerable entities like NPCs, sentinels....
     *
     * @param source      Player targeted another entity
     * @param target      Entity being targeted
     * @param interaction Type of interaction, whether it's positive (buff, heal) or negative (offense skill, attack)
     * @return If false, any interaction should be cancelled!
     */
    @Deprecated
    boolean canTarget(Player source, LivingEntity target, InteractionType interaction);
}
