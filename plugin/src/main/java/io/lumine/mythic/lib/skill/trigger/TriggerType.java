package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class TriggerType {

    // Combat

    /**
     * Called when a player kills an entity
     * Metadata target is the entity dying
     */
    @NotNull public static TriggerType KILL_ENTITY = new TriggerType("KILL_ENTITY"),

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
     * Called when a player casts a skill (e.g MMOCore skill
     * casting system)
     */
    CAST = new TriggerType("CAST"),

    /**
     * Should be used by plugins when skills get triggered by
     * another cause not listed in {@link TriggerType}
     */
    API = new TriggerType("API");


    public TriggerType(@NotNull String internal_name) { this(internal_name, true); }
    public TriggerType(@NotNull String internal_name, boolean silent) {
        name = internal_name;
        this.silent = silent;
    }

    /**
     * When set to false, any skill with this trigger type should
     * send a message to the player if this skill cannot be used.
     */
    public boolean isSilent() { return silent; }
    private final boolean silent;

    public String getName() { return UtilityMethods.caseOnWords(name().toLowerCase().replace("_", " ")); }
    public String getLowerCaseId() { return name().toLowerCase().replace("_", "-"); }

    /**
     * @param format The string trying to convert
     *
     * @return The trigger type of this name
     *
     * @throws IllegalArgumentException If the string does not correspond to a trigger type
     */
    @NotNull public static TriggerType valueOf(@Nullable String format) throws IllegalArgumentException {
        TriggerType ret = safeValueOf(format);
        if (ret == null) { throw new IllegalArgumentException("No trigger type of name '" + format + "' found. "); }
        return ret;
    }

    public static void registerAll() {
        register(KILL_ENTITY);
        register(ATTACK);
        register(DAMAGED);
        register(DAMAGED_BY_ENTITY);
        register(DEATH);
        register(SHOOT_BOW);
        register(ARROW_TICK);
        register(ARROW_HIT);
        register(ARROW_LAND);

        register(RIGHT_CLICK);
        register(LEFT_CLICK);
        register(SHIFT_RIGHT_CLICK);
        register(SHIFT_LEFT_CLICK);

        register(LOGIN);
        register(SNEAK);
        register(CAST);
        register(API);
    }
    public static void register(@NotNull TriggerType trigger) { triggers.add(trigger); }
    @NotNull public static ArrayList<TriggerType> values() { return triggers; }
    @NotNull static ArrayList<TriggerType> triggers = new ArrayList<>();

    /**
     * Must use this method in order to allow use triggers of {@link TimerTrigger} type.
     *
     * @param format String you want to convert to a trigger.
     *               Usually exactly the trigger name.
     *
     * @return The string, parsed, if it made sense.
     */
    @Nullable public static TriggerType safeValueOf(@Nullable String format) {
        if (format == null) { return null; }

        // Is it?
        for (TriggerType type : values()) { if (type.is(format)) { return type; } }

        // No base matched, lets try the special ones
        if (format.startsWith("TIMER_")) {

            // Strip that
            Integer time = SilentNumbers.IntegerParse(format.substring("TIMER_".length()));

            // Valid?
            if(time != null && time > 0) {

                // Make one of those chad timer triggers
                return TimerTrigger.newTimerTrigger(time);
            }
        }
        return null;
    }

    /**
     * @param format String that we are trying to match
     *
     * @return If this trigger is the one named after the string.
     */
    public boolean is(@NotNull String format) { return name.equals(format); }

    /**
     * @return This trigger type serialized into a string, undoes {@link #safeValueOf(String)}
     */
    @Override public String toString() { return name(); }

    /**
     * @return Identical to {@link #toString()}
     */
    @NotNull public String name() { return name; }
    @NotNull String name;
}
