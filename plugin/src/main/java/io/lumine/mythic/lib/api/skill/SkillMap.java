package io.lumine.mythic.lib.api.skill;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.skill.handler.SkillHandler;

import java.util.HashMap;
import java.util.Map;

public class SkillMap {
    private final MMOPlayerData mmoData;
    /**
     * This map enables to calculate the skill buffs associated to a particular skill and a particular skillModifier
     * without having to parse other modifers. In particular this is done every time a skill is cast.
     */
    private final Map<String, SkillInstance> skillInstanceMap = new HashMap<>();

    /**
     * This map is used to access directly the skill buffs to remove them or modify them.
     * SkillBuff triggers can then access and modify the value of a trigger in O(1).
     */
    private final Map<String, SkillBuff> skillBuffMap = new HashMap<>();

    public SkillMap(MMOPlayerData mmoData) {
        this.mmoData = mmoData;
        for (SkillHandler skillHandler : MythicLib.inst().getSkills().getHandlers()) {
            skillInstanceMap.put(skillHandler.getId(), new SkillInstance(skillHandler));
        }
    }

    public SkillInstance getSkillInstance(String skill) {
        //When a skillHandler is registered from an external plugin and is not thus in the initial map we add it.
        if (!skillInstanceMap.containsKey(skill)) {
            SkillHandler handler = MythicLib.plugin.getSkills().getHandler(skill);
            if (handler != null)
                skillInstanceMap.put(skill, new SkillInstance(handler));
        }
        return skillInstanceMap.get(skill);
    }

    public boolean hasSkillBuff(String key) {
        return skillBuffMap.containsKey(key);
    }

    public SkillBuff getSkillBuff(String key) {
        return skillBuffMap.get(key);
    }

    public void addSkillBuff(SkillBuff skillBuff) {
        skillBuffMap.put(skillBuff.getKey(), skillBuff);
    }

    public void removeSkillBuff(String key) {
        skillBuffMap.remove(key);

    }


}
