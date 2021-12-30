package io.lumine.mythic.lib.skill.handler;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.result.MythicMobsSkillResult;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.skills.Skill;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;

public class MythicMobsSkillHandler extends SkillHandler<MythicMobsSkillResult> {
    private Skill skill;

    public MythicMobsSkillHandler(String id, FileConfiguration config) {
        super(id);

        String skillName = config.getString("mythicmobs-skill-id");
        Validate.notNull(skillName, "Could not find MM skill name");

        Optional<Skill> opt = MythicMobs.inst().getSkillManager().getSkill(skillName);
        Validate.isTrue(opt.isPresent(), "Could not find MM skill with name '" + skillName + "'");
        skill = opt.get();

        if (config.contains("modifiers"))
            registerModifiers(config.getStringList("modifiers"));
    }

    public String getInternalName() {
        return skill.getInternalName();
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Skill getSkill() {
        return skill;
    }

    @Override
    public MythicMobsSkillResult getResult(SkillMetadata meta) {
        return new MythicMobsSkillResult(meta, this);
    }

    @Override
    public void whenCast(MythicMobsSkillResult result, SkillMetadata skillMeta) {
        result.getMythicMobskillMetadata().getVariables().putObject("MMOSkil", skillMeta.getStats());

        skill.execute(result.getMythicMobskillMetadata());
    }
}
