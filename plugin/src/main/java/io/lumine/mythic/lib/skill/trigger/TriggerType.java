package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.UtilityMethods;

public enum TriggerType {

    // Combat

    /**
     * Called when a player kills an entity
     * Metadata target is the entity dying
     */
    KILL_ENTITY,

    /**
     * Called when a player attacks any other entity
     * Metadata target is the entity being attacked
     */
    ATTACK,

    /**
     * Called when a player is damaged by any source
     */
    DAMAGED,

    /**
     * Called when a player is damaged by an entity (explosion/attack)
     * Skill target is the entity damaging the player
     */
    DAMAGED_BY_ENTITY,

    /**
     * Called when a player dies
     */
    DEATH,

    // Bow or crossbows

    /**
     * Called when an arrow is shot from a bow
     * Skill target is the arrow fired
     */
    SHOOT_BOW,

    /**
     * Called every tick when an arrow flies in the air
     * Skill target is the arrow fired
     */
    ARROW_TICK,

    /**
     * Called when an arrow hits an entity
     * Skill target is the entity hit (the arrow dies right afterwards)
     */
    ARROW_HIT,

    /**
     * Called when an arrow hits a block
     * Skill target is the arrow fired
     */
    ARROW_LAND,

    // Trident

    /**
     * Called when a player shoots a trident
     * Skill target is the thrown trident
     */
    SHOOT_TRIDENT,

    /**
     * Called every tick when a trident flies in the air
     * Skill target is the thrown trident
     */
    TRIDENT_TICK,

    /**
     * Called when an arrow hits an entity
     * Skill target is the thrown trident
     */
    TRIDENT_HIT,

    /**
     * Called when a thrown trident hits a block
     * Skill target is the trident thrown
     */
    TRIDENT_LAND,

    // Clicks

    /**
     * Called when a player right clicks
     * This trigger displays cooldown/mana restriction messages.
     */
    RIGHT_CLICK(false),

    /**
     * Called when a player left clicks
     * This trigger displays cooldown/mana restriction messages.
     */
    LEFT_CLICK(false),

    /**
     * Called when a player right clicks while crouching
     * This trigger displays cooldown/mana restriction messages.
     */
    SHIFT_RIGHT_CLICK(false),

    /**
     * Called when a player left clicks while crouching
     * This trigger displays cooldown/mana restriction messages.
     */
    SHIFT_LEFT_CLICK(false),

    // Misc

    /**
     * Called when a player logs in
     */
    LOGIN,

    /**
     * This trigger type is never called. It can be used
     * as a trigger type for passive skills
     */
    @Deprecated
    PASSIVE,

    /**
     * Called when a player toggles on or off sneaking.
     * This trigger displays cooldown/mana restriction messages.
     */
    SNEAK(false),

    /**
     * Called when a player casts a skill (e.g MMOCore skill
     * casting system)
     */
    CAST,

    /**
     * Should be used by plugins when skills get triggered by
     * another cause not listed in {@link TriggerType}
     */
    API;

    /**
     * When set to false, any skill with this trigger type should
     * send a message to the player if this skill cannot be used.
     */
    private final boolean silent;

    TriggerType() {
        this(true);
    }

    TriggerType(boolean silent) {
        this.silent = silent;
    }

    public boolean isSilent() {
        return silent;
    }

    public String getName() {
        return UtilityMethods.caseOnWords(name().toLowerCase().replace("_", " "));
    }

    public String getLowerCaseId() {
        return name().toLowerCase().replace("_", "-");
    }

    public static TriggerType safeValueOf(String format) {
        for (TriggerType type : values())
            if (type.name().equals(format))
                return type;
        return null;
    }
}
