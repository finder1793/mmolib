package io.lumine.mythic.lib.skill.handler;

import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.result.SkillResult;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The interface that groups up:
 * - custom MM skills
 * - custom MythicLib skills (not implemented yet)
 * - custom SkillAPI skills (not implemented yet)
 * - default MythicLib skills
 * <p>
 * MythicLib has a huge default collection of "skills" however
 * these cannot be cast yet because they still lack important
 * properties which cannot be setup in MythicLib because they
 * are specific to the plugins using the ML skills:
 * - the ability configurable name and description (different setups for MI and MMOCore)
 * - default values for modifiers (also different formulas for MI and MMOCore)
 * <p>
 * MythicLib stores {@link SkillHandler}, which are skills substracted from
 * all of this data which is SPECIFIC to the plugin using the skills.
 * Other plugins like MMOCore and MMOItems store {@link Skill} instances.
 *
 * @param <T> Skill result class being used by that skill behaviour
 * @author jules
 */
public abstract class SkillHandler<T extends SkillResult> {
    private final String id;
    private final Set<String> modifiers = new HashSet<>();

    protected static final Random random = new Random();

    public SkillHandler() {
        this.id = getClass().getSimpleName().toUpperCase().replace("-", "_").replaceAll("[^A-Z_]", "");

        registerModifiers("cooldown", "mana", "stamina");
    }

    public SkillHandler(String id) {
        this.id = id;

        registerModifiers("cooldown", "mana", "stamina");
    }

    public String getId() {
        return id;
    }

    public void registerModifiers(String... mods) {
        registerModifiers(Arrays.asList(mods));
    }

    public void registerModifiers(Collection<String> mods) {
        modifiers.addAll(mods);
    }

    /**
     * Skill modifiers are specific numeric values that
     * determine how powerful a skill is. Modifiers can be
     * the skill damage, cooldown, duration if it applies
     * some potion effect, etc.
     * <p>
     * MythicLib does NOT store modifier default values/
     * formulas that scale with the player class level. Rather
     * it only stores what modifiers the skill has because
     * it's a necessary information for skill handlers.
     *
     * @return The set of all possible modifiers of that skill
     */
    public Set<String> getModifiers() {
        return modifiers;
    }

    /**
     * Skill results are used to check if a skill can be cast.
     * <p>
     * This method evaluates MythicMobs custom conditions,
     * checks if the caster has an entity in their line of
     * sight, if he is on the ground...
     * <p>
     * Runs first before {@link Skill#getResult(SkillMetadata)}
     *
     * @param meta Info of skill being cast
     * @return Skill result
     */
    @NotNull
    public abstract T getResult(SkillMetadata meta);

    /**
     * This is where the actual skill effects are applied.
     * <p>
     * Runs last, after {@link Skill#whenCast(SkillMetadata)}
     *
     * @param result    Skill result
     * @param skillMeta Info of skill being cast
     */
    public abstract void whenCast(T result, SkillMetadata skillMeta);
}
