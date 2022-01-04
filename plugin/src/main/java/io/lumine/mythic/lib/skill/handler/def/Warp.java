package io.lumine.mythic.lib.skill.handler.def;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.SkillResult;
import io.lumine.mythic.lib.util.ParabolicProjectile;
import io.lumine.mythic.lib.version.VersionSound;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

public class Warp extends SkillHandler<Warp.WarpSkillResult> {
    public Warp() {
        super();

        registerModifiers("range");
    }

    @NotNull
    @Override
    public WarpSkillResult getResult(SkillMetadata meta) {
        return new WarpSkillResult(meta);
    }

    @Override
    public void whenCast(WarpSkillResult result, SkillMetadata skillMeta) {
        Player caster = skillMeta.getCaster().getPlayer();

        caster.getWorld().playSound(caster.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 2);

        Location loc = result.getLocation();
        loc.setYaw(caster.getPlayer().getLocation().getYaw());
        loc.setPitch(caster.getPlayer().getLocation().getPitch());

        new ParabolicProjectile(caster.getPlayer().getLocation().add(0, 1, 0), loc.clone().add(0, 1, 0), () -> {
            if (caster.getPlayer().isOnline() && !caster.getPlayer().isDead()) {
                caster.getPlayer().teleport(loc);
                caster.getPlayer().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, caster.getPlayer().getLocation().add(0, 1, 0), 0);
                caster.getPlayer().getWorld().spawnParticle(Particle.SPELL_INSTANT, caster.getPlayer().getLocation().add(0, 1, 0), 32, 0, 0, 0, .1);
                caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), VersionSound.ENTITY_ENDERMAN_TELEPORT.toSound(), 1, 1);
            }
        }, 2, Particle.SPELL_INSTANT);
    }

    public class WarpSkillResult implements SkillResult {
        private final Location loc;

        public WarpSkillResult(SkillMetadata meta) {
            Player caster = meta.getCaster().getPlayer();
            RayTraceResult res = caster.rayTraceBlocks(50, FluidCollisionMode.NEVER);
            loc = res == null ? null : res.getHitPosition().toLocation(caster.getWorld()).subtract(caster.getEyeLocation());
        }

        @Override
        public boolean isSuccessful(SkillMetadata skillMeta) {
            return loc != null;
        }

        public Location getLocation() {
            return loc;
        }
    }
}
