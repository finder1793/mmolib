package io.lumine.mythic.lib.api.skill;

import io.lumine.mythic.lib.skill.handler.SkillHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SkillInstance {
    private final SkillHandler skillHandler;
    private final Map<String, SkillModifierInstance> skillModifierMap = new HashMap<>();

    public SkillInstance(SkillHandler skillHandler) {
        this.skillHandler = skillHandler;
        Set<String> modifiers=skillHandler.getModifiers();
        for(String mod: modifiers)
            skillModifierMap.put(mod,new SkillModifierInstance(mod));
    }

    public SkillModifierInstance getSkillModifier(String skillModifier) {
        return skillModifierMap.get(skillModifier);
    }

}
