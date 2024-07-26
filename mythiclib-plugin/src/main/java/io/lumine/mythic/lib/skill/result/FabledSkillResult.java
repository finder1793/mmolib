package io.lumine.mythic.lib.skill.result;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.skill.SkillMetadata;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import studio.magemonkey.fabled.Fabled;
import studio.magemonkey.fabled.api.player.PlayerData;
import studio.magemonkey.fabled.api.skills.Skill;
import studio.magemonkey.fabled.api.skills.SkillShot;
import studio.magemonkey.fabled.api.skills.TargetSkill;
import studio.magemonkey.fabled.api.target.TargetHelper;

import javax.annotation.Nullable;
import java.util.Objects;

public class FabledSkillResult implements SkillResult {
    private final PlayerData skillPlayerData;
    private final Skill skill;
    private final int level;
    private final boolean success;

    @Nullable
    private LivingEntity target;

    public FabledSkillResult(@NotNull SkillMetadata skillMeta, @NotNull Skill skill) {
        this.skillPlayerData = Fabled.getData(skillMeta.getCaster().getPlayer());
        this.skill = skill;
        this.level = (int) skillMeta.getParameter("level");
        this.success = checkUsage(skillMeta);
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    @NotNull
    public LivingEntity getTarget() {
        return Objects.requireNonNull(target, "Skill has no target");
    }

    @Override
    public boolean isSuccessful() {
        return success;
    }

    private boolean checkUsage(SkillMetadata skillMeta) {

        // Dead players can't cast skills
        if (UtilityMethods.isInvalidated(skillMeta.getCaster())) return false;

        // Skill Shots
        if (skill instanceof SkillShot) return true;

            // Target Skills
        else if (skill instanceof TargetSkill) {
            target = skillMeta.hasTargetEntity() && skillMeta.getTargetEntityOrNull() instanceof LivingEntity ? (LivingEntity) skillMeta.getTargetEntity() : TargetHelper.getLivingTarget(skillPlayerData.getPlayer(), skill.getRange(level));

            // Must have a target
            if (target == null) return false;
        }

        return false;
    }
}
