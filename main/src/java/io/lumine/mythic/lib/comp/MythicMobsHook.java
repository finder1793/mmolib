package io.lumine.mythic.lib.comp;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.entity.Entity;

public class MythicMobsHook {
    public static String getFaction(Entity e) {
        ActiveMob mob = MythicMobs.inst().getMobManager().getMythicMobInstance(e);
        if (mob != null)
            if (mob.hasFaction())
                return mob.getFaction();
        return null;
    }

}
