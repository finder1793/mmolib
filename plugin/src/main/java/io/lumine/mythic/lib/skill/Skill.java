package io.lumine.mythic.lib.skill;

import io.lumine.mythic.lib.api.event.skill.PlayerCastSkillEvent;
import io.lumine.mythic.lib.api.event.skill.SkillCastEvent;
import io.lumine.mythic.lib.player.cooldown.CooldownObject;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.SkillResult;
import io.lumine.mythic.lib.skill.trigger.TriggerMetadata;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Implemented by MMOItems abilities or MMOCore class skills.
 * <p>
 * This class implements all skill restrictions and behaviours
 * that are SPECIFIC to MMOItems or MMOCore like resource costs,
 * cooldown messages, no-cooldown modes...
 *
 * @author jules
 */
public abstract class Skill implements CooldownObject {
    private final TriggerType trigger;

    public Skill(TriggerType trigger) {
        this.trigger = Objects.requireNonNull(trigger, "Trigger cannot be null");
    }

    public SkillResult cast(TriggerMetadata triggerMeta) {
        return cast(triggerMeta.toSkillMetadata(this));
    }

    public <T extends SkillResult> SkillResult cast(SkillMetadata meta) {

        SkillHandler<T> handler = (SkillHandler<T>) getHandler();

        // Lower level skill restrictions
        T result = handler.getResult(meta);
        if (!result.isSuccessful(meta))
            return result;

        // High level skill restrictions
        if (!getResult(meta))
            return result;

        // Call first Bukkit event
        PlayerCastSkillEvent called1 = new PlayerCastSkillEvent(meta, result);
        Bukkit.getPluginManager().callEvent(called1);
        if (called1.isCancelled())
            return result;

        // High level skill effects
        whenCast(meta);

        // Lower level skill effects
        handler.whenCast(result, meta);

        // Call second Bukkit event
        Bukkit.getPluginManager().callEvent(new SkillCastEvent(meta, result));

        return result;
    }

    /**
     * This method should be used to check for resource costs
     * or other skill limitations.
     * <p>
     * Runs last after {@link SkillHandler#getResult(SkillMetadata)}
     *
     * @param skillMeta Info of skill being cast
     * @return If the skill can be cast
     */
    @NotNull
    public abstract boolean getResult(SkillMetadata skillMeta);

    /**
     * This is NOT where the actual skill effects are applied.
     * <p>
     * This method should be used to handle resource costs
     * or cooldown messages if required.
     * <p>
     * Runs first before {@link SkillHandler#whenCast(SkillResult, SkillMetadata)}
     *
     * @param skillMeta Info of skill being cast
     */
    public abstract void whenCast(SkillMetadata skillMeta);

    public abstract SkillHandler<?> getHandler();

    /**
     * This contains the following information:
     * - whether or not the skill is active or passive
     * - whether or not the skill is silent
     *
     * @return Context in which this skill is triggered
     */
    @NotNull
    public TriggerType getTrigger() {
        return trigger;
    }

    public abstract double getModifier(String path);

    @Override
    public String getCooldownPath() {
        return "skill_" + getHandler().getId();
    }
}
