package io.lumine.mythic.lib.skill.trigger;

import org.jetbrains.annotations.Nullable;

/**
 * This acts as an interface between MMOItems item abilities,
 * MythicLib custom skills, MMOCore player skills as well as
 * MythicMobs custom abilities.
 *
 * @author indyuce
 */
@FunctionalInterface
public interface TriggeredSkill {

    /**
     * Called when the skill is triggered.
     *
     * @param triggerMeta All the info required to cast a skill
     */
    void execute(@Nullable TriggerMetadata triggerMeta);
}
