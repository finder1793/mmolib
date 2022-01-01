package io.lumine.mythic.lib.skill.handler.def.passive;

import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.def.SimpleSkillResult;
import io.lumine.mythic.lib.skill.trigger.PassiveSkill;
import io.lumine.mythic.lib.skill.trigger.TriggerMetadata;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Backstab extends SkillHandler<SimpleSkillResult> implements Listener {
    public Backstab() {
        super(false);

        registerModifiers("extra");
    }

    @Override
    public SimpleSkillResult getResult(SkillMetadata meta) {
        return new SimpleSkillResult(meta.hasAttackBound() && meta.hasTargetEntity() && meta.getTargetEntityOrNull() instanceof LivingEntity);
    }

    @Override
    public void whenCast(SimpleSkillResult result, SkillMetadata skillMeta) {
        LivingEntity target = (LivingEntity) skillMeta.getTargetEntityOrNull();
        skillMeta.getAttack().getDamage().multiply(1 + skillMeta.getModifier("extra") / 100, DamageType.PHYSICAL);
        target.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, target.getHeight() / 2, 0), 32, 0, 0, 0, .05);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1, 1.5f);
    }

    @EventHandler
    public void a(PlayerAttackEvent event) {
        MMOPlayerData data = event.getData();
        LivingEntity target = event.getEntity();
        if (!event.getAttack().getDamage().hasType(DamageType.WEAPON)
                || event.getPlayer().getEyeLocation().getDirection().angle(target.getEyeLocation().getDirection()) > Math.PI / 6)
            return;

        PassiveSkill skill = data.getPassiveSkill(this);
        if (skill == null)
            return;

        skill.getTriggeredSkill().cast(new TriggerMetadata(event.getAttack(), event.getEntity()));
    }
}
