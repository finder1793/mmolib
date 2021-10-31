package io.lumine.mythic.lib.comp.target;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * This class was updated to support SaberFactions. The only
 * change was the Relation import:
 * <p>
 * Old:
 * import com.massivecraft.factions.perms.Relation;
 * <p>
 * New:
 * import com.massivecraft.factions.struct.Relation;
 */
public class FactionsRestriction implements TargetRestriction {

    @Override
    public boolean canTarget(Player source, LivingEntity target, InteractionType interaction) {

        if (interaction.isOffense() && target instanceof Player) {
            FPlayer fTarget = FPlayers.getInstance().getByPlayer((Player) target);
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(source);

            Relation relation = fTarget.getRelationTo(fPlayer);
            if (relation == Relation.ALLY || relation == Relation.MEMBER)
                return false;
        }

        return true;
    }
}
