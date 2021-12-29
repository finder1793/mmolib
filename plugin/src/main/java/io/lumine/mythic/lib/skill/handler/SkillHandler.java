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
 * Other plugins like MMOCore and MMOItems store {@link Skill}.
 *
 * @param <T> Skill result class being used by that skill behaviour
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

    public Set<String> getModifiers() {
        return modifiers;
    }

    @NotNull
    public abstract T getResult(SkillMetadata meta);

    public abstract void cast(T result, SkillMetadata skillMeta);
}
