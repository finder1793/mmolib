package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackCategory;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.skill.Skill;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public enum DamageType {

    /**
     * Magic damage dealt by magic weapons or abilities
     */
    MAGIC("\u00a79"),

    /**
     * Physical damage dealt by melee attacks or skills
     */
    PHYSICAL("\u00a78"),

    /**
     * Damage dealt by any type of weapon
     */
    WEAPON("\u00a77"),

    /**
     * Damage dealt by skills or abilities
     */
    SKILL("\u00a7f"),

    /**
     * Projectile based weapons or skills
     */
    PROJECTILE("\u00a7a"),

    /**
     * Hitting an enemy with bare hands
     */
    UNARMED("\u00a7e"),

    /**
     * For use with {@link io.lumine.mythic.lib.comp.mythicmobs.mechanic.MMODamageMechanic}
     * and {@link io.lumine.mythic.lib.comp.mythicmobs.condition.HasDamageTypeCondition}
     * to make on-hit skills that inflict damage but don't infinitely loop themselves.
     */
    ON_HIT("\u00a70"),

    /**
     * For use with {@link io.lumine.mythic.lib.comp.mythicmobs.mechanic.MMODamageMechanic}
     * and {@link io.lumine.mythic.lib.comp.mythicmobs.condition.HasDamageTypeCondition}
     * to make summoner class abilities, supposing you had a system for it built with MythicMobs
     * or another plugin (GooP Maybe!??);
     */
    MINION("\u00a7d"),

    /**
     * Damage over time
     */
    DOT("\u00a73");


    public String getPath() {
        return name().toLowerCase();
    }

    public String getOffenseStat() {
        return name() + "_DAMAGE";
    }

    DamageType() {this("\u00a71"); }

    /**
     * For gunging debugging purposes that he really likes
     * making everything colorful and pretty (this guy).
     * <br><br>
     * Now also used in MMOItems to display 'Damage Type Restriction'
     *
     * @param col Color displayed when using {@link DamagePacket#toString()}
     */
    DamageType(@NotNull String col) {this.col = col; }

    @NotNull final String col;
    @NotNull public String getColor() { return col; }

    //region SKMOD Encoding

    /**
     * This method will compare the types of damage being dealt during an attack,
     * to the types of damage expected by the Damage Type skill modifier.
     *
     * @param meta Meta with damage types to evaluate
     *
     * @return If it matches the {@link io.lumine.mythic.lib.skill.handler.SkillHandler#SKMOD_DAMAGE_TYPE} requirement
     */
    public static boolean matchesAttackMeta(@Nullable AttackMetadata meta, @NotNull Skill triggeredSkill) {

        // If there is no meta, the trigger doesn't support these modifiers and succeeds automatically
        if (meta == null) {
            //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 NULL\u00a7a successor");
            return true; }

        // Get Damage Requirement
        long encodedDamageRequirements = Math.round(triggeredSkill.getModifier(io.lumine.mythic.lib.skill.handler.SkillHandler.SKMOD_DAMAGE_TYPE));
        //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 Metadata " + meta.getDamage().toString());

        /*
         * To correctly respect the default values of MINION, DOT, and ON_HIT, RAW success was disabled.
         *
         * This is because we agree that the default value 0 means !MINION !DOT !ON_HIT, to prevent infinite loops
         * and other AFK forms of calling skills without the server admins explicitly allowing these skills to
         * trigger by these special damage types.
         *
         *
            //TGG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 Matching \u00a7e" + damageReq + "\u00a77 to " + (meta == null ? "null" : meta.getDamage().toString()));
            if (encodedDamageRequirements == 0) {
                //TGG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 RAW\u00a7a success");
                return true; }
        */

        // OR mode, as opposed to AND
        boolean orMode = encodedDamageRequirements < 0;
        if (orMode) { encodedDamageRequirements *= -1; }

        // Build
        ArrayList<DamageType> white = getWhitelist(encodedDamageRequirements);

        // Default to blacklist looping/automated damage types
        if (meta.getDamage().hasType(DamageType.MINION) && !white.contains(DamageType.MINION)) {
            //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 MINION Defaults to Reject");
            return false; }
        if (meta.getDamage().hasType(DamageType.DOT) && !white.contains(DamageType.DOT)) {
            //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 DOT Defaults to Reject");
            return false; }
        if (meta.getDamage().hasType(DamageType.ON_HIT) && !white.contains(DamageType.ON_HIT)) {
            //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 ON_HIT Defaults to Reject");
            return false; }
        
        /*
         * Nothing else defined? Allow through.
         */
        if (encodedDamageRequirements == 0) {
            //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 RAW\u00a7a success");
            return true; }

        // Get the blacklist I guess
        ArrayList<DamageType> black = getBlacklist(encodedDamageRequirements);

        // Any blacklisted damage causes a failure
        for (DamageType blacklisted : black) { if (meta.getDamage().hasType(blacklisted)) {
            //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 BCK\u00a7c failure\u00a7e " + blacklisted.toString());
            return false; } }

        if (orMode) {

            // Meeting any whitelisted type causes a success
            for (DamageType whitelisted : white) { if (meta.getDamage().hasType(whitelisted)) {
                //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 OR WHT\u00a7a success\u00a7e " + whitelisted.toString());
                return true; } }

            // None succeeded
            //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 OR\u00a7c failure");
            return false;

        // All must match
        } else {

            // Any missing requirement causes a failure
            for (DamageType whitelisted : white) { if (!meta.getDamage().hasType(whitelisted)) {
                //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 AND WHT\u00a7c failure\u00a7e " + whitelisted.toString());
                return false; } }

            // All succeeded
            //TRG//MythicCraftingManager.log("\u00a78TRIG\u00a73 " + triggeredSkill.getTrigger().toString() + "\u00a77 AND\u00a7a success");
            return true;
        }
    }

    /**
     * Using prime numbers, we are able to encode multiple damage types into a single number.
     * Every other prime number is a blacklist, which will prevent skills from triggering.
     *
     * @param encodedDamageRequirements Assumed to be positive value, the encoded blacklist number.
     *
     * @return The Blacklist this number encodes for
     *
     * @see #encodeDamageTypeMatch(ArrayList, ArrayList, boolean), {@link #encodeDamageTypeMatch(String, FriendlyFeedbackProvider)}
     */
    @NotNull public static ArrayList<DamageType> getBlacklist(double encodedDamageRequirements) {

        // Build
        ArrayList<DamageType> black = new ArrayList<>();
        if (encodedDamageRequirements == 0) { return black; }

        // Scry damage types
        if (encodedDamageRequirements % 3 == 0) { black.add(DamageType.MAGIC); }
        if (encodedDamageRequirements % 7 == 0) { black.add(DamageType.PHYSICAL); }
        if (encodedDamageRequirements % 13 == 0) { black.add(DamageType.WEAPON); }
        if (encodedDamageRequirements % 19 == 0) { black.add(DamageType.SKILL); }
        if (encodedDamageRequirements % 29 == 0) { black.add(DamageType.PROJECTILE); }
        if (encodedDamageRequirements % 37 == 0) { black.add(DamageType.UNARMED); }
        if (encodedDamageRequirements % 43 == 0) { black.add(DamageType.ON_HIT); }
        if (encodedDamageRequirements % 53 == 0) { black.add(DamageType.MINION); }
        if (encodedDamageRequirements % 61 == 0) { black.add(DamageType.DOT); }

        return black;
    }

    /**
     * Using prime numbers, we are able to encode multiple damage types into a single number.
     * Every other prime number is a whitelist, which will be required to allow triggering of skills.
     *
     * @param encodedDamageRequirements Assumed to be positive value, the encoded whitelist number.
     *
     * @return The Whitelist this number encodes for.
     *
     * @see #encodeDamageTypeMatch(ArrayList, ArrayList, boolean), {@link #encodeDamageTypeMatch(String, FriendlyFeedbackProvider)}
     */
    @NotNull public static ArrayList<DamageType> getWhitelist(double encodedDamageRequirements) {

        // Build
        ArrayList<DamageType> white = new ArrayList<>();
        if (encodedDamageRequirements == 0) { return white; }

        // Scry damage types
        if (encodedDamageRequirements % 2 == 0) { white.add(DamageType.MAGIC); }
        if (encodedDamageRequirements % 5 == 0) { white.add(DamageType.PHYSICAL); }
        if (encodedDamageRequirements % 11 == 0) { white.add(DamageType.WEAPON); }
        if (encodedDamageRequirements % 17 == 0) { white.add(DamageType.SKILL); }
        if (encodedDamageRequirements % 23 == 0) { white.add(DamageType.PROJECTILE); }
        if (encodedDamageRequirements % 31 == 0) { white.add(DamageType.UNARMED); }
        if (encodedDamageRequirements % 41 == 0) { white.add(DamageType.ON_HIT); }
        if (encodedDamageRequirements % 47 == 0) { white.add(DamageType.MINION); }
        if (encodedDamageRequirements % 59 == 0) { white.add(DamageType.DOT); }

        return white;
    }

    /**
     * The human-friendly way of specifying damage type requirements, a
     * space-separated list of {@link DamageType}s.
     * <br><br>
     * If the very first item is the keyword "OR", the OR operator will
     * be used to compare whitelisted damage types instead of AND.
     * <br><br>
     * To blacklist damage types instead, use the "!" prefix.
     *
     * @param message String in the format 'OR MAGIC WEAPON !SKILL'
     *
     * @param ffp Reasoning for why this failed, if it failed
     *
     * @return Built encoded double ready to be used in decoding methods, or null if the message format is ugly.
     */
    @Nullable public static Double encodeDamageTypeMatch(@Nullable String message, @Nullable FriendlyFeedbackProvider ffp) {
        if (message == null) {

            // Mention
            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR, "Message was null. ");

            // L
            return null;
        }

        boolean ormode = false;
        if (message.startsWith("OR ")) {

            // Or mode
            ormode = true;
            message = message.substring("OR ".length());
        }

        // Build lists
        ArrayList<DamageType> white = new ArrayList<>();
        ArrayList<DamageType> black = new ArrayList<>();

        boolean failure = false;
        String[] split = message.split(" ");
        for (String sp : split) {

            try {

                boolean negation = false;
                String cropped = sp;
                if (sp.startsWith("!")) { cropped = sp.substring(1); negation = true; }

                // Get Damage Type
                DamageType observed = DamageType.valueOf(cropped);
                if (negation) { black.add(observed); } else { white.add(observed); }

            } catch (IllegalArgumentException ignored) {

                // Mention
                FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR, "Unknown damage type '$i{0}$b'. ", sp);

                // L
                failure = true;
            }
        }

        if (failure) { return null; }
        return encodeDamageTypeMatch(white, black, ormode);
    }

    /**
     * This method will turn a list of whitelisted and blacklisted damage
     * types into a single number! Ready to be saved as a skill modifier.
     *
     * @param whitelist Damage Types to require
     * @param blacklist Damage Types to avoid
     * @param orMode If the comparison is OR
     *
     * @return Built encoded double ready to be used in decoding methods.
     */
    public static double encodeDamageTypeMatch(@NotNull ArrayList<DamageType> whitelist, @NotNull ArrayList<DamageType> blacklist, boolean orMode) {

        double ret = 1;
        if (orMode) { ret = -1; }

        /*
         * Just multiply the number however
         */
        for (DamageType type : DamageType.values()) {

            switch (type) {
                case MAGIC:
                    if (whitelist.contains(type)) { ret *= 2; }
                    else if (blacklist.contains(type)) { ret *= 3; }
                    break;
                case PHYSICAL:
                    if (whitelist.contains(type)) { ret *= 5; }
                    else if (blacklist.contains(type)) { ret *= 7; }
                    break;
                case WEAPON:
                    if (whitelist.contains(type)) { ret *= 11; }
                    else if (blacklist.contains(type)) { ret *= 13; }
                    break;
                case SKILL:
                    if (whitelist.contains(type)) { ret *= 17; }
                    else if (blacklist.contains(type)) { ret *= 19; }
                    break;
                case PROJECTILE:
                    if (whitelist.contains(type)) { ret *= 23; }
                    else if (blacklist.contains(type)) { ret *= 29; }
                    break;
                case UNARMED:
                    if (whitelist.contains(type)) { ret *= 31; }
                    else if (blacklist.contains(type)) { ret *= 37; }
                    break;
                case ON_HIT:
                    if (whitelist.contains(type)) { ret *= 41; }
                    else if (blacklist.contains(type)) { ret *= 43; }
                    break;
                case MINION:
                    if (whitelist.contains(type)) { ret *= 47; }
                    else if (blacklist.contains(type)) { ret *= 53; }
                    break;
                case DOT:
                    if (whitelist.contains(type)) { ret *= 59; }
                    else if (blacklist.contains(type)) { ret *= 61; }
                    break;
            }
        }

        return ret;
    }
    //endregion
}
