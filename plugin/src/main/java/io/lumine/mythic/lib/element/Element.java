package io.lumine.mythic.lib.element;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.skill.custom.CustomSkill;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

public class Element {
    private final String id, name;
    private final CustomSkill criticalStrike, regularAttack;

    public Element(ConfigurationSection config) {
        Validate.isTrue(config.contains("name"), "Please specify an element name");
        Validate.isTrue(config.contains("regular-attack"), "Please provide a skill cast on regular elemental attacks");

        this.id = config.getName();
        this.name = config.getString("name");
        this.criticalStrike = config.contains("crit-strike") ? MythicLib.plugin.getSkills().loadCustomSkill("crit-strike") : null;
        this.regularAttack = config.contains("crit-strike") ? MythicLib.plugin.getSkills().loadCustomSkill("regular-attack") : null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CustomSkill getSkill(boolean criticalStrike) {
        return criticalStrike && this.criticalStrike != null ? this.criticalStrike : regularAttack;
    }

    public String getUpperCaseId() {
        return id.toUpperCase().replace("-", "_");
    }
}
