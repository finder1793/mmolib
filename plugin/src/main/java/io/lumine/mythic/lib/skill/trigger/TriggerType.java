package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.UtilityMethods;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TriggerType {

    // Combat

    /**
     * Called when a player kills an entity
     * Metadata target is the entity dying
     */
    @NotNull
    public static TriggerType KILL_ENTITY = new TriggerType("KILL_ENTITY"),

    /**
     * Called when a player attacks any other entity
     * Metadata target is the entity being attacked
     */
    ATTACK = new TriggerType("ATTACK"),

    /**
     * Called when a player is damaged by any source
     */
    DAMAGED = new TriggerType("DAMAGED"),

    /**
     * Called when a player is damaged by an entity (explosion/attack)
     * Skill target is the entity damaging the player
     */
    DAMAGED_BY_ENTITY = new TriggerType("DAMAGED_BY_ENTITY"),

    /**
     * Called when a player dies
     */
    DEATH = new TriggerType("DEATH"),

    // Bow or crossbows

    /**
     * Called when an arrow is shot from a bow
     * Skill target is the arrow fired
     */
    SHOOT_BOW = new TriggerType("SHOOT_BOW"),

    /**
     * Called every tick when an arrow flies in the air
     * Skill target is the arrow fired
     */
    ARROW_TICK = new TriggerType("ARROW_TICK"),

    /**
     * Called when an arrow hits an entity
     * Skill target is the entity hit (the arrow dies right afterwards)
     */
    ARROW_HIT = new TriggerType("ARROW_HIT"),

    /**
     * Called when an arrow hits a block
     * Skill target is the arrow fired
     */
    ARROW_LAND = new TriggerType("ARROW_LAND"),

    // Trident

    /**
     * Called when a player shoots a trident
     * Skill target is the thrown trident
     */
    SHOOT_TRIDENT = new TriggerType("SHOOT_TRIDENT"),

    /**
     * Called every tick when a trident flies in the air
     * Skill target is the thrown trident
     */
    TRIDENT_TICK = new TriggerType("TRIDENT_TICK"),

    /**
     * Called when an arrow hits an entity
     * Skill target is the thrown trident
     */
    TRIDENT_HIT = new TriggerType("TRIDENT_HIT"),

    /**
     * Called when a thrown trident hits a block
     * Skill target is the trident thrown
     */
    TRIDENT_LAND = new TriggerType("TRIDENT_LAND"),

    // Clicks

    /**
     * Called when a player right clicks
     * This trigger displays cooldown/mana restriction messages.
     */
    RIGHT_CLICK = new TriggerType("RIGHT_CLICK", false),

    /**
     * Called when a player left clicks
     * This trigger displays cooldown/mana restriction messages.
     */
    LEFT_CLICK = new TriggerType("LEFT_CLICK", false),

    /**
     * Called when a player right clicks while crouching
     * This trigger displays cooldown/mana restriction messages.
     */
    SHIFT_RIGHT_CLICK = new TriggerType("SHIFT_RIGHT_CLICK", false),

    /**
     * Called when a player left clicks while crouching
     * This trigger displays cooldown/mana restriction messages.
     */
    SHIFT_LEFT_CLICK = new TriggerType("SHIFT_LEFT_CLICK", false),

    // Misc

    /**
     * Called when a player logs in
     */
    LOGIN = new TriggerType("LOGIN"),

    /**
     * Called when a player toggles on or off sneaking.
     * This trigger displays cooldown/mana restriction messages.
     */
    SNEAK = new TriggerType("SNEAK", false),

    /**
     * Casts the skill at regular time intervals. Timer period
     * can be edited using the corresponding skill modifier.
     */
    TIMER,

    /**
     * Called when a player casts a skill (e.g MMOCore skill
     * casting system)
     */
    CAST = new TriggerType("CAST"),

    /**
     * Should be used by plugins when skills get triggered by
     * another cause not listed in {@link TriggerType}
     */
    API = new TriggerType("API");

    private final String id;
    private final boolean silent;

    private static final Map<String, TriggerType> BY_ID = new HashMap<>();

    static {
        register(KILL_ENTITY);
        register(ATTACK);
        register(DAMAGED);
        register(DAMAGED_BY_ENTITY);
        register(DEATH);

        register(SHOOT_BOW);
        register(ARROW_TICK);
        register(ARROW_HIT);
        register(ARROW_LAND);

        register(SHOOT_TRIDENT);
        register(TRIDENT_TICK);
        register(TRIDENT_HIT);
        register(TRIDENT_LAND);

        register(RIGHT_CLICK);
        register(LEFT_CLICK);
        register(SHIFT_RIGHT_CLICK);
        register(SHIFT_LEFT_CLICK);

        register(LOGIN);
        register(SNEAK);
        register(CAST);
        register(API);
    }

    public TriggerType(String id) {
        this(id, true);
    }

    public TriggerType(String id, boolean silent) {
        this.id = id;
        this.silent = silent;
    }

    /**
     * @return Identical to {@link #toString()}
     */
    @NotNull
    public String name() {
        return id;
    }

    /**
     * When set to false, any skill with this trigger type should
     * send a message to the player if this skill cannot be used.
     */
    public boolean isSilent() {
        return silent;
    }

    public String getName() {
        return UtilityMethods.caseOnWords(name().toLowerCase().replace("_", " "));
    }

    public String getLowerCaseId() {
        return name().toLowerCase().replace("_", "-");
    }

    /**
     * @return This trigger type serialized into a string
     */
    @Override
    public String toString() {
        return name();
    }

    /**
     * @param id The string trying to convert
     * @return The trigger type of this name
     * @throws IllegalArgumentException If the string does not correspond to a trigger type
     */
    @NotNull
    public static TriggerType valueOf(@Nullable String id) {
        return Objects.requireNonNull(BY_ID.get(id), "Could not find trigger type with ID '" + id + "'");
    }

    public static void register(TriggerType trigger) {
        BY_ID.put(trigger.name(), trigger);
    }

    public static Collection<TriggerType> values() {
        return BY_ID.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriggerType that = (TriggerType) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
