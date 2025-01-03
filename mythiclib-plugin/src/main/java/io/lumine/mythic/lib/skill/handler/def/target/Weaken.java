package io.lumine.mythic.lib.skill.handler.def.target;

import io.lumine.mythic.lib.api.event.AttackEvent;
import io.lumine.mythic.lib.api.util.TemporaryListener;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.def.TargetSkillResult;
import io.lumine.mythic.lib.util.ParabolicProjectile;
import io.lumine.mythic.lib.util.SmallParticleEffect;
import io.lumine.mythic.lib.version.VParticle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

public class Weaken extends SkillHandler<TargetSkillResult> {
    public Weaken() {
        super();

        registerModifiers("ratio", "duration");
    }

    @Override
    public TargetSkillResult getResult(SkillMetadata meta) {
        return new TargetSkillResult(meta);
    }

    @Override
    public void whenCast(TargetSkillResult result, SkillMetadata skillMeta) {
        LivingEntity target = result.getTarget();
        Player caster = skillMeta.getCaster().getPlayer();

        new ParabolicProjectile(caster.getPlayer().getLocation().add(0, 1, 0), target.getLocation().add(0, target.getHeight() / 2, 0), randomVector(caster.getPlayer()), () -> {
            if (!target.isDead())
                new Weakened(target, skillMeta.getParameter("ratio"), skillMeta.getParameter("duration"));
        }, 2, VParticle.WITCH.get());
    }

    private Vector randomVector(Player player) {
        double a = Math.toRadians(player.getEyeLocation().getYaw() + 90);
        a += (RANDOM.nextBoolean() ? 1 : -1) * (RANDOM.nextDouble() + .5) * Math.PI / 6;
        return new Vector(Math.cos(a), .8, Math.sin(a)).normalize().multiply(.4);
    }

    public static class Weakened extends TemporaryListener {
        private final Entity entity;
        private final double coef;

        public Weakened(Entity entity, double ratio, double duration) {
            this.entity = entity;
            this.coef = 1 + ratio / 100;

            new SmallParticleEffect(entity, VParticle.WITCH.get());

            close((long) (duration * 20));
        }

        @EventHandler
        public void a(AttackEvent event) {
            if (event.getEntity().equals(entity)) {
                event.getEntity().getWorld().spawnParticle(VParticle.WITCH.get(), entity.getLocation().add(0, entity.getHeight() / 2, 0), 16, .5, .5, .5, 0);
                event.getDamage().multiplicativeModifier(coef);
            }
        }
    }
}
