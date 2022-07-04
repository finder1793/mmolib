package io.lumine.mythic.lib.comp.target;

import net.citizensnpcs.api.npc.NPC;
import org.mcmonkey.sentinel.SentinelTrait;

public class SentinelTargetRestriction {

    public boolean isSentinel(NPC npc) {
        return npc.hasTrait(SentinelTrait.class);
    }
}
