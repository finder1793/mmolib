package io.lumine.mythic.lib.comp.target;

import cc.javajobs.factionsbridge.FactionsBridge;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.FactionsAPI;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Relationship;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Supports both SaberFactions and FactionsUUID and any other Factions based core
 */
public class FactionsRestriction implements TargetRestriction {

    @Override
    public boolean canTarget(Player source, LivingEntity target, InteractionType interaction) {

        if (!(target instanceof Player))
            return true;

        FactionsAPI api = FactionsBridge.getFactionsAPI();
        Faction faction = api.getFaction(source);
        if (faction == null)
            return true;

        Relationship relation = faction.getRelationshipTo(api.getFPlayer((Player) target));
        return isEnemy(relation) == interaction.isOffense();
    }

    private boolean isEnemy(Relationship relation) {
        return relation == Relationship.ENEMY || relation == Relationship.NONE;
    }
}
