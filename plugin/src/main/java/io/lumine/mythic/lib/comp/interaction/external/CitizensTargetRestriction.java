package io.lumine.mythic.lib.comp.interaction.external;

import io.lumine.mythic.lib.comp.interaction.InteractionType;
import io.lumine.mythic.lib.comp.interaction.TargetRestriction;
import io.lumine.mythic.lib.comp.interaction.external.SentinelTargetRestriction;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CitizensTargetRestriction implements TargetRestriction {
    private final SentinelTargetRestriction sentinelsRestriction;

    public CitizensTargetRestriction() {
        sentinelsRestriction = Bukkit.getPluginManager().getPlugin("Sentinel") == null ? null : new SentinelTargetRestriction();
    }

    @Override
    public boolean canTarget(Player source, LivingEntity entity, InteractionType interaction) {

        // No npc
        NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
        if (npc == null)
            return true;

        // Sentinel
        if (sentinelsRestriction != null && sentinelsRestriction.isSentinel(npc))
            return true;

        // Not protected
        return !npc.data().get(NPC.DEFAULT_PROTECTED_METADATA, true);
    }
}
