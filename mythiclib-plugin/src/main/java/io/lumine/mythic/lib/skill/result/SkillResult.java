package io.lumine.mythic.lib.skill.result;

import io.lumine.mythic.lib.skill.SkillMetadata;

/**
 * When the player tries to cast a skill either through
 * MMOCore or MMOItems, a skill result instance is created.
 * <p>
 * This instance determines if that skill can be cast under
 * the circumstances provided by the SkillMetadata.
 * <p>
 * Skill results share similar features with custom
 * skill conditions as well as skill targeters.
 */
public interface SkillResult {

    /**
     * @param skillMeta Information required to cast a skill
     * @return If the ability is cast successfully. This method is used to apply
     *         extra ability conditions (player must be on the ground, must aim
     *         at an entity..)
     */
    public boolean isSuccessful(SkillMetadata skillMeta);
}
