package io.lumine.mythic.lib.script;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.util.PostLoadObject;
import io.lumine.mythic.lib.script.mechanic.Mechanic;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.condition.Condition;
import io.lumine.mythic.lib.util.configobject.ConfigSectionObject;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Custom skill made using the ML skill creation system
 */
public class Script extends PostLoadObject {
    private final String id;
    private final boolean publicSkill;

    private final List<Condition> conditions = new ArrayList<>();
    private final List<Mechanic> mechanics = new ArrayList<>();

    public Script(@NotNull ConfigurationSection config) {
        super(config);

        this.id = config.getName();
        publicSkill = config.getBoolean("public", false);
    }

    public Script(String id, boolean publicSkill) {
        super(null);

        this.id = id;
        this.publicSkill = publicSkill;
    }

    @Override
    public void whenPostLoaded(@NotNull ConfigurationSection config) {

        // Load conditions
        if (config.isConfigurationSection("conditions"))
            for (String str : config.getConfigurationSection("conditions").getKeys(false))
                try {
                    conditions.add(MythicLib.plugin.getSkills().loadCondition(new ConfigSectionObject(config.getConfigurationSection("conditions." + str))));
                } catch (RuntimeException exception) {
                    MythicLib.plugin.getLogger().log(Level.WARNING, "Could not load condition '" + str + "' from script '" + id + "': " + exception.getMessage());
                }

        // Load mechanics
        if (config.isConfigurationSection("mechanics"))
            for (String str : config.getConfigurationSection("mechanics").getKeys(false))
                try {
                    mechanics.add(MythicLib.plugin.getSkills().loadMechanic(new ConfigSectionObject(config.getConfigurationSection("mechanics." + str))));
                } catch (RuntimeException exception) {
                    MythicLib.plugin.getLogger().log(Level.WARNING, "Could not load mechanic '" + str + "' from script '" + id + "': " + exception.getMessage());
                }

    }

    public String getId() {
        return id;
    }

    /**
     * Should the skill be public i.e should a skill handler register for
     * that custom skill. If not, server admins won't be able to access
     * this skill once it is registered in the skill manager.
     * <p>
     * It's true for more convenience by default. This means that users
     * can disable the display of some skills which they consider like
     * 'intermediate' skills and not real skills.
     *
     * @return If the skill should be accessible to other plugins
     */
    public boolean isPublic() {
        return publicSkill;
    }

    public List<Mechanic> getMechanics() {
        return mechanics;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    /**
     * Cast this skill
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
