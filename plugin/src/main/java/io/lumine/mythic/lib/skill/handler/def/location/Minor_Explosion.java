package io.lumine.mythic.lib.skill.handler.def.location;

import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.def.LocationSkillResult;
import io.lumine.mythic.lib.UtilityMethods;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Minor_Explosion extends SkillHandler<LocationSkillResult> {
    public Minor_Explosion() {
        super();

        registerModifiers("damage", "knockback", "radius");
    }

    @Override
    public LocationSkillResult getResult(SkillMetadata meta) {
        return new LocationSkillResult(meta);
    }

    @Override
    public void whenCast(LocationSkillResult result, SkillMetadata skillMeta) {
        Location loc = result.getTarget();
        Player caster = skillMeta.getCaster().getPlayer();

        double damage = skillMeta.getModifier("damage");
        double radiusSquared = Math.pow(skillMeta.getModifier("radius"), 2);
        double knockback = skillMeta.getModifier("knockback");

        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc.add(0, .1, 0), 32, 1.7, 1.7, 1.7, 0);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 64, 0, 0, 0, .3);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 2, 1);

        for (Entity entity : UtilityMethods.getNearbyChunkEntities(loc))
            if (entity.getLocation().distanceSquared(loc) < radiusSquared && UtilityMethods.canTarget(caster, entity)) {
                new AttackMetadata(new DamageMetadata(damage, DamageType.SKILL, DamageType.MAGIC), skillMeta.getCaster()).damage((LivingEntity) entity);
                entity.setVelocity(normalize(entity.getLocation().subtract(loc).toVector().setY(0)).setY(.2).multiply(2 * knockback));
            }
    }

    private Vector normalize(Vector vec) {
        return vec.lengthSquared() == 0 ? vec : vec.normalize();
    }
}
