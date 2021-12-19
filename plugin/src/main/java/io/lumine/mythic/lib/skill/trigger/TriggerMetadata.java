package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.damage.AttackMetadata;
import org.bukkit.entity.Entity;

@Deprecated
public class TriggerMetadata {
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
    public TriggerMetadata(AttackMetadata attack, Entity target) {
        this.attack = attack;
        this.target = target;
    }

    public AttackMetadata getAttack() {
        return attack;
    }

    public Entity getTarget() {
        return target;
    }
}
