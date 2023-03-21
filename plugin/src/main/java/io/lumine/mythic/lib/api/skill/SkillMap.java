package io.lumine.mythic.lib.api.skill;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.skill.handler.SkillHandler;

import java.util.HashMap;
import java.util.Map;

public class SkillMap {
    private final MMOPlayerData mmoData;
    private final Map<String,SkillInstance> skillInstanceMap = new HashMap<>();

    public SkillMap(MMOPlayerData mmoData) {
        this.mmoData = mmoData;
        for(SkillHandler skillHandler: MythicLib.inst().getSkills().getHandlers()){
            skillInstanceMap.put(skillHandler.getId(),new SkillInstance(skillHandler));
        }
    }

    public SkillInstance getSkillInstance(String skill) {
        return skillInstanceMap.get(skill);
    }
}
