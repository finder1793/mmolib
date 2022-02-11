package io.lumine.mythic.lib.skill.handler.def.target;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.def.TargetSkillResult;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class Combo_Attack extends SkillHandler<TargetSkillResult> {
    public Combo_Attack() {
        super();

        registerModifiers("damage", "count");
    }

    @Override
    public TargetSkillResult getResult(SkillMetadata meta) {
        return new TargetSkillResult(meta, 10, InteractionType.OFFENSE_SKILL);
    }

    @Override
    public void whenCast(TargetSkillResult result, SkillMetadata skillMeta) {
        new BukkitRunnable() {
            final int count = (int) skillMeta.getModifier("count");
            final double damage = skillMeta.getModifier("damage") / count;
            final LivingEntity target = result.getTarget();

            int c = 0;

            @Override
            public void run() {
                if (c++ > count || skillMeta.getCaster().getData().isOnline()) {
                    cancel();
                    return;
                }

                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1, 2);
                target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, target.getHeight() / 2, 0), 24, 0, 0, 0, .7);
                skillMeta.attack(target, damage, DamageType.SKILL, DamageType.PHYSICAL);
            }
        }.runTaskTimer(MythicLib.plugin, 0, 5);
    }
}
