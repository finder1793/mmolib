package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.player.PlayerMetadata;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.variable.VariableList;
import io.lumine.mythic.lib.skill.custom.variable.VariableScope;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TriggerMetadata extends PlayerMetadata {
    private final AttackMetadata attack;
    private final Entity target;

    /**
     * Instantiated every time a player performs an action linked
     * to a skill trigger. This is used to temporarily cache the
     * player stats and save the info needed to cast some skills
     *
     * @param attack Either the current attackMeta when the trigger type is DAMAGE for instance,
     *               or an empty one for any other trigger type.
     * @param target Potential skill target
     */
    public TriggerMetadata(@NotNull AttackMetadata attack, @Nullable Entity target) {
        super(attack);

        this.attack = attack;
        this.target = target;
    }

    /**
     * Instantiated every time a player performs an action linked
     * to a skill trigger. This is used to temporarily cache the
     * player stats and save the info needed to cast some skills
     *
     * @param attacker Attacker metadata
     * @param attack   Either the current attackMeta when the trigger type is DAMAGE for instance,
     *                 or null for other trigger types
     * @param target   Potential skill target
     */
    public TriggerMetadata(@NotNull PlayerMetadata attacker, @Nullable AttackMetadata attack, @Nullable Entity target) {
        super(attacker);

        this.attack = attack;
        this.target = target;
    }

    @Nullable
    public AttackMetadata getAttack() {
        return attack;
    }

    @Nullable
    public Entity getTarget() {
        return target;
    }

    /**
     * Called when casting a skill. This transfers all the useful
     * information into a skill metadata
     *
     * @param cast Skill being cast
     * @return Skill cast information containing all the previous information
     */
    public SkillMetadata toSkillMetadata(Skill cast) {
        return new SkillMetadata(cast, this, new VariableList(VariableScope.SKILL), attack, getPlayer().getLocation(), null, target);
    }
}
