package io.lumine.mythic.lib.comp.interaction.external;

import net.citizensnpcs.api.npc.NPC;
import org.mcmonkey.sentinel.SentinelTrait;

public class SentinelTargetRestriction {

    public boolean isSentinel(NPC npc) {
        return npc.hasTrait(SentinelTrait.class);
    }
}
