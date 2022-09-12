package io.lumine.mythic.lib.skill.handler;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.skills.Skill;
import com.sucy.skill.api.skills.SkillShot;
import com.sucy.skill.api.skills.TargetSkill;
import com.sucy.skill.log.Logger;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.result.SkillAPISkillResult;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class SkillAPISkillHandler extends SkillHandler<SkillAPISkillResult> {
    private final Skill skill;

    public SkillAPISkillHandler(ConfigurationSection config) {
        super(config.getString("skillapi-skill-id"));

        String skillName = config.getString("skillapi-skill-id");
        this.skill = Objects.requireNonNull(SkillAPI.getSkill(skillName), "Could not find SkillAPI skill with name '" + skillName + "'");

        registerModifiers("level");
    }

    public Skill getSkill() {
        return skill;
    }

    @Override
    public SkillAPISkillResult getResult(SkillMetadata meta) {
        return new SkillAPISkillResult(meta, skill);
    }

    @Override
    public void whenCast(SkillAPISkillResult result, SkillMetadata skillMeta) {

        // Skill Shots
        if (skill instanceof SkillShot)
            try {
                ((SkillShot) skill).cast(skillMeta.getCaster().getPlayer(), result.getLevel());
            } catch (Exception exception) {
                Logger.bug("Failed to cast skill - " + skill.getName() + ": Internal skill error");
                exception.printStackTrace();
            }

            // Target Skills
        else if (skill instanceof TargetSkill)
            try {
                final boolean isAlly = !SkillAPI.getSettings().canAttack(skillMeta.getCaster().getPlayer(), result.getTarget());
                ((TargetSkill) skill).cast(skillMeta.getCaster().getPlayer(), result.getTarget(), result.getLevel(), isAlly);
            } catch (Exception exception) {
                Logger.bug("Failed to cast skill - " + skill.getName() + ": Internal skill error");
                exception.printStackTrace();
            }
    }
}
