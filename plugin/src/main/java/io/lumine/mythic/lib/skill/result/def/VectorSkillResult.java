package io.lumine.mythic.lib.skill.result.def;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.result.SkillResult;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VectorSkillResult implements SkillResult {
    private final Vector target;

    public VectorSkillResult(SkillMetadata skillMeta) {
        this.target = getTargetDirection(skillMeta.getCaster().getPlayer(), skillMeta.getTargetEntityOrNull());
    }

    public Vector getTarget() {
        return target;
    }

    public Vector getTargetDirection(Player player, Entity target) {
        return target == null ? player.getEyeLocation().getDirection() : target.getLocation().add(0, target.getHeight() / 2, 0).subtract(player.getLocation().add(0, 1.3, 0)).toVector().normalize();
    }

    @Override
    public boolean isSuccessful(SkillMetadata skillMeta) {
        return true;
    }
}
