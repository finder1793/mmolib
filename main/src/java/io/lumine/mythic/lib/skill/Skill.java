package io.lumine.mythic.lib.skill;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.util.PostLoadObject;
import io.lumine.mythic.lib.skill.condition.Condition;
import io.lumine.mythic.lib.skill.mechanic.Mechanic;
import io.lumine.mythic.lib.util.ConfigObject;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Skill extends PostLoadObject {
    private final String name;

    private final List<Condition> conditions = new ArrayList<>();
    private final List<Mechanic> mechanics = new ArrayList<>();

    public Skill(ConfigurationSection config) {
        super(config);

        this.name = config.getName();
    }

    @Override
    public void whenPostLoaded(ConfigurationSection config) {

        // Load conditions
        if (config.isConfigurationSection("conditions"))
            for (String str : config.getConfigurationSection("conditions").getKeys(false))
                try {
                    conditions.add(MythicLib.plugin.getSkills().loadCondition(new ConfigObject(config.getConfigurationSection("conditions." + str))));
                } catch (RuntimeException exception) {
                    MythicLib.plugin.getLogger().log(Level.WARNING, "Could not load condition '" + str + "' from skill '" + name + "': " + exception.getMessage());
                }

        // Load mechanics
        if (config.isConfigurationSection("mechanics"))
            for (String str : config.getConfigurationSection("mechanics").getKeys(false))
                try {
                    mechanics.add(MythicLib.plugin.getSkills().loadMechanic(new ConfigObject(config.getConfigurationSection("mechanics." + str))));
                } catch (RuntimeException exception) {
                    MythicLib.plugin.getLogger().log(Level.WARNING, "Could not load mechanic '" + str + "' from skill '" + name + "': " + exception.getMessage());
                }

    }

    public String getName() {
        return name;
    }

    public List<Mechanic> getMechanics() {
        return mechanics;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    /**
     * Called when casting the root skill
     *
     * @param playerData Player data of skill caster
     * @return If conditions are met ie if the skill was cast
     */
    public boolean cast(MMOPlayerData playerData) {
        return cast(new SkillMetadata(this, playerData));
    }

    /**
     * Casts this skill
     *
     * @param meta Skill cast information
     * @return If conditions are met ie skill is cast
     */
    public boolean cast(SkillMetadata meta) {

        for (Condition condition : conditions)
            if (!condition.checkIfMet(meta))
                return false;

        new MechanicQueue(meta, this).next();
        return true;
    }
}
