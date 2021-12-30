package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.player.PlayerMetadata;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.variable.VariableList;
import io.lumine.mythic.lib.skill.custom.variable.VariableScope;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TriggerMetadata extends PlayerMetadata {
    private final AttackMetadata attack;
    private final Entity target;

    /**
     * Instantiated every time a player performs an action linked
     * to a skill trigger. This is used to temporarily cache the player stats
     * and save the info needed to cast some skills
     *
     * @param attack Either the current attackMeta when the trigger type is DAMAGE for instance,
     *               or an empty one for any other trigger type.
     * @param target Potential skill target
     */
    public TriggerMetadata(@NotNull AttackMetadata attack, @Nullable Entity target) {
        super(attack.getStats());

        this.attack = attack;
        this.target = target;
    }

    @NotNull
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
     * @param cast  Skill being cast
     * @return Skill cast information containing all the previous information
     */
    public SkillMetadata toSkillMetadata(Skill cast) {
        return new SkillMetadata(cast, attack.getStats(), new VariableList(VariableScope.SKILL), this.attack, attack.getPlayer().getLocation(), null, target);
    }

    /**
     * Utility method that makes a player deal damage to a specific
     * entity. This creates the attackMetadata based on the data
     * stored by the CasterMetadata, and calls it using MythicLib
     * damage manager
     *
     * @param target Target entity
     * @param damage Damage dealt
     * @param types  Type of target
     * @return The (modified) attack metadata
     */
    public AttackMetadata attack(LivingEntity target, double damage, DamageType... types) {
        AttackMetadata attackMeta = new AttackMetadata(new DamageMetadata(damage, types), attack.getStats());
        MythicLib.plugin.getDamage().damage(attackMeta, target);
        return attackMeta;
    }
}
