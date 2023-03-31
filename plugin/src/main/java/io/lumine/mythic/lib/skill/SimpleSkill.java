package io.lumine.mythic.lib.skill;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Can be used to cast a skill handler with configurable modifier input
 *
 * @author jules
 */
public class SimpleSkill extends Skill {
    private final SkillHandler<?> handler;
    private final Map<String, Double> modifiers = new HashMap<>();

    public SimpleSkill(TriggerType trigger, SkillHandler<?> handler) {
        super(trigger);

        this.handler = handler;
    }

    @NotNull
    @Override
    public boolean getResult(SkillMetadata skillMeta) {
        return true;
    }

    @Override
    public void whenCast(SkillMetadata skillMeta) {
        // Nothing here
    }

    @Override
    public SkillHandler<?> getHandler() {
        return handler;
    }

    @Override
    public double getModifier(String path, MMOPlayerData playerData) {
        return playerData.getSkillBuffMap().getSkillInstance(getHandler().getId())
                .getSkillModifier(path).getTotal(modifiers.getOrDefault(path, 0d));
    }

    public void registerModifier(String path, double value) {
        modifiers.put(path, value);
    }
}
