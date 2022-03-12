package io.lumine.mythic.lib.skill.result;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.GenericCaster;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import io.lumine.mythic.core.skills.SkillTriggers;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.MythicMobsSkillHandler;

import java.util.HashSet;

public class MythicMobsSkillResult implements SkillResult {
    private final MythicMobsSkillHandler behaviour;
    private final SkillMetadataImpl mmSkillMeta;

    public MythicMobsSkillResult(SkillMetadata skillMeta, MythicMobsSkillHandler behaviour) {
        this.behaviour = behaviour;

        // TODO what's the difference between trigger and caster.
        AbstractEntity trigger = BukkitAdapter.adapt(skillMeta.getCaster().getPlayer());
        SkillCaster caster = new GenericCaster(trigger);

        HashSet<AbstractEntity> targetEntities = new HashSet<>();
        HashSet<AbstractLocation> targetLocations = new HashSet<>();

        if (skillMeta.hasTargetEntity())
            targetEntities.add(BukkitAdapter.adapt(skillMeta.getTargetEntityOrNull()));

        if (skillMeta.hasTargetLocation())
            targetLocations.add(BukkitAdapter.adapt(skillMeta.getTargetLocationOrNull()));

        mmSkillMeta = new SkillMetadataImpl(SkillTriggers.API, caster, trigger, BukkitAdapter.adapt(skillMeta.getCaster().getPlayer().getEyeLocation()), targetEntities, targetLocations, 1);

        // Stats & cast skill are cached inside a variable
        mmSkillMeta.getVariables().putObject("MMOStatMap", skillMeta.getCaster());
        mmSkillMeta.getVariables().putObject("MMOSkill", skillMeta.getCast());
        if (skillMeta.hasAttackBound())
            mmSkillMeta.getVariables().putObject("MMOAttack", skillMeta.getAttack());
    }

    @Override
    public boolean isSuccessful(SkillMetadata skillMeta) {
        return behaviour.getSkill().isUsable(mmSkillMeta);
    }

    public SkillMetadataImpl getMythicMobskillMetadata() {
        return mmSkillMeta;
    }
}
