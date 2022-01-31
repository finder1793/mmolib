package io.lumine.mythic.lib.skill.handler;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.comp.anticheat.CheatType;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.result.MythicMobsSkillResult;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.skills.Skill;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MythicMobsSkillHandler extends SkillHandler<MythicMobsSkillResult> {
    private final Skill skill;

    /**
     * Maps the amount of ticks during which the anticheat
     * must stop checking for hacks; for every cheat type
     */
    private final Map<CheatType, Integer> antiCheat = new HashMap<>();

    public MythicMobsSkillHandler(ConfigurationSection config) {
        super(config, config.getName().isEmpty() ? config.getString("mythicmobs-skill-id") : config.getName());

        String skillName = config.getString("mythicmobs-skill-id");

        Optional<Skill> opt = MythicMobs.inst().getSkillManager().getSkill(skillName);
        Validate.isTrue(opt.isPresent(), "Could not find MM skill with name '" + skillName + "'");
        skill = opt.get();

        if (config.isConfigurationSection("disable-anti-cheat"))
            for (String key : config.getConfigurationSection("disable-anti-cheat").getKeys(false)) {
                CheatType cheatType = CheatType.valueOf(key.toUpperCase().replace(" ", "_").replace("-", "_"));
                this.antiCheat.put(cheatType, config.getInt("disable-anti-cheat." + key));
            }
    }

    public String getInternalName() {
        return skill.getInternalName();
    }

    public Skill getSkill() {
        return skill;
    }

    public Map<CheatType, Integer> getAntiCheat() {
        return antiCheat;
    }

    @Override
    public MythicMobsSkillResult getResult(SkillMetadata meta) {
        return new MythicMobsSkillResult(meta, this);
    }

    @Override
    public void whenCast(MythicMobsSkillResult result, SkillMetadata skillMeta) {

        // Disable anticheat
        if (MythicLib.plugin.hasAntiCheat())
            MythicLib.plugin.getAntiCheat().disableAntiCheat(skillMeta.getCaster().getPlayer(), antiCheat);

        skill.execute(result.getMythicMobskillMetadata());
    }
}
