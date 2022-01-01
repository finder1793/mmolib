package io.lumine.mythic.lib.skill.handler.def.target;

import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.def.TargetSkillResult;
import org.bukkit.entity.LivingEntity;

public class Smite extends SkillHandler<TargetSkillResult> {
    public Smite() {
        super();

        registerModifiers("cooldown", "damage");
    }

    @Override
    public TargetSkillResult getResult(SkillMetadata meta) {
        return new TargetSkillResult(meta);
    }

    @Override
    public void whenCast(TargetSkillResult result, SkillMetadata skillMeta) {
        LivingEntity target = result.getTarget();
        new AttackMetadata(new DamageMetadata(skillMeta.getModifier("damage"), DamageType.SKILL, DamageType.MAGIC), skillMeta.getCaster()).damage(target);
        target.getWorld().strikeLightningEffect(target.getLocation());
    }
}
