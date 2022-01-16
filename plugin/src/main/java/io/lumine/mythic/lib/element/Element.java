package io.lumine.mythic.lib.element;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.skill.SimpleSkill;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.Objects;

public class Element {
    private final String id, name;
    private final SkillHandler<?> criticalStrike, regularAttack;

    public Element(ConfigurationSection config) {
        Validate.notNull(config, "Config cannot be null");

        this.id = config.getName();
        this.name = Objects.requireNonNull(config.getString("name"), "Please specify an element name");
        this.regularAttack = MythicLib.plugin.getSkills().loadSkillHandler(Objects.requireNonNull(config.get("regular-attack"), "Could not find skill for regular attacks"));
        this.criticalStrike = config.contains("crit-strike") ? MythicLib.plugin.getSkills().loadSkillHandler(config.get("crit-strike")) : null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Skill getSkill(boolean criticalStrike) {
        SkillHandler<?> handler = criticalStrike && this.criticalStrike != null ? this.criticalStrike : regularAttack;
        return handler == null ? null : new SimpleSkill(handler);
    }

    public String getUpperCaseId() {
        return id.toUpperCase().replace("-", "_");
    }
}
