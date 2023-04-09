package io.lumine.mythic.lib.player.skillmod;

import io.lumine.mythic.lib.api.stat.api.ModifiedInstance;

public class SkillModifierInstance extends ModifiedInstance<SkillModifier> {
    private final String skillModifier;

    public SkillModifierInstance(String skillModifier) {
        this.skillModifier = skillModifier;
    }

    public String getSkillModifier() {
        return skillModifier;
    }
}
