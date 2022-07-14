package io.lumine.mythic.lib.element;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.skill.SimpleSkill;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Element {
    private final String id, name;
    private final Material icon;
    private final SkillHandler<?> criticalStrike, regularAttack;

    public Element(ConfigurationSection config) {
        Validate.notNull(config, "Config cannot be null");

        this.id = config.getName();
        this.icon = Material.valueOf(UtilityMethods.enumName(config.getString("icon", "DIRT")));
        this.name = Objects.requireNonNull(config.getString("name"), "Please specify an element name");
        this.regularAttack = MythicLib.plugin.getSkills().loadSkillHandler(Objects.requireNonNull(config.get("regular-attack"), "Could not find skill for regular attacks"));
        this.criticalStrike = config.contains("crit-strike") ? MythicLib.plugin.getSkills().loadSkillHandler(config.get("crit-strike")) : null;
    }

    public String getId() {
        return id;
    }

    public String getUpperCaseId() {
        return UtilityMethods.enumName(id);
    }

    public String getName() {
        return name;
    }

    // Useful for MMOItems
    public Material getIcon() {
        return icon;
    }

    @NotNull
    public Skill getSkill(boolean criticalStrike) {
        final SkillHandler<?> handler = criticalStrike && this.criticalStrike != null ? this.criticalStrike : regularAttack;
        return new SimpleSkill(TriggerType.API, handler);
    }
}
