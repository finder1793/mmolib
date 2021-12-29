package io.lumine.mythic.lib.skill.result;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.MythicMobsSkillHandler;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;

import java.util.HashSet;

public class MythicMobsSkillResult implements SkillResult {
    private final MythicMobsSkillHandler behaviour;
    private final io.lumine.xikage.mythicmobs.skills.SkillMetadata mmSkillMeta;

    public MythicMobsSkillResult(SkillMetadata skillMeta, MythicMobsSkillHandler behaviour) {
        this.behaviour = behaviour;

        // TODO what's the difference between trigger and caster.
        AbstractEntity trigger = BukkitAdapter.adapt(skillMeta.getCaster().getPlayer());
        SkillCaster caster = new GenericCaster(trigger);

        HashSet<AbstractEntity> targetEntities = new HashSet<>();
        HashSet<AbstractLocation> targetLocations = new HashSet<>();

        if (skillMeta.hasTargetEntity())
            targetEntities.add(BukkitAdapter.adapt(skillMeta.getTargetEntityOrNull()));

        mmSkillMeta = new io.lumine.xikage.mythicmobs.skills.SkillMetadata(SkillTrigger.CAST, caster, trigger, BukkitAdapter.adapt(skillMeta.getCaster().getPlayer().getEyeLocation()), targetEntities, targetLocations, 1);

        // Stats are cached inside a variable
        mmSkillMeta.getVariables().putObject("MMOStatMap", skillMeta.getStats());
    }

    @Override
    public boolean isSuccessful(SkillMetadata skillMeta) {
        return behaviour.getSkill().isUsable(mmSkillMeta);
    }

    public io.lumine.xikage.mythicmobs.skills.SkillMetadata getMythicMobskillMetadata() {
        return mmSkillMeta;
    }
}
