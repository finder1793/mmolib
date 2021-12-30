package io.lumine.mythic.lib.skill.handler.def.simple;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.def.SimpleSkillResult;
import io.lumine.mythic.lib.version.VersionSound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Blink extends SkillHandler<SimpleSkillResult> {
    public Blink() {
        super();

        registerModifiers("range");
    }

    @Override
    public SimpleSkillResult getResult(SkillMetadata meta) {
        return new SimpleSkillResult();
    }

    @Override
    public void whenCast(SimpleSkillResult result, SkillMetadata skillMeta) {
        Player caster = skillMeta.getCaster().getPlayer();

        caster.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, caster.getLocation().add(0, 1, 0), 0);
        caster.getWorld().spawnParticle(Particle.SPELL_INSTANT, caster.getLocation().add(0, 1, 0), 32, 0, 0, 0, .1);
        caster.getWorld().playSound(caster.getLocation(), VersionSound.ENTITY_ENDERMAN_TELEPORT.toSound(), 1, 1);
        Location loc = caster.getTargetBlock(null, (int) skillMeta.getModifier("range")).getLocation().add(0, 1, 0);
        loc.setYaw(caster.getLocation().getYaw());
        loc.setPitch(caster.getLocation().getPitch());
        caster.teleport(loc);
        caster.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, caster.getLocation().add(0, 1, 0), 0);
        caster.getWorld().spawnParticle(Particle.SPELL_INSTANT, caster.getLocation().add(0, 1, 0), 32, 0, 0, 0, .1);
    }
}
