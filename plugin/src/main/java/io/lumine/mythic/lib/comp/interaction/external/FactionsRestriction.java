package io.lumine.mythic.lib.comp.interaction.external;

import cc.javajobs.factionsbridge.FactionsBridge;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.FactionsAPI;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Relationship;
import io.lumine.mythic.lib.comp.interaction.InteractionType;
import io.lumine.mythic.lib.comp.interaction.TargetRestriction;
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
        return relation == Relationship.NONE || ((relation == Relationship.ENEMY) == interaction.isOffense());
    }
}
