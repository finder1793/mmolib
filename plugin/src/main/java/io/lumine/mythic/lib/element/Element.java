package io.lumine.mythic.lib.element;

import io.lumine.xikage.mythicmobs.skills.Skill;

public class Element {
    private String id, name;
    private Skill criticalStrike;

    /**
     * Particle being displayed when attacking
     */
    private Object particleEffect;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUpperCaseId() {
        return id.toUpperCase().replace("-", "_");
    }
}
