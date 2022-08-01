package io.lumine.mythic.lib.player.skill;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.player.modifier.ModifierMap;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.handler.def.passive.Backstab;
import io.lumine.mythic.lib.skill.trigger.TriggerMetadata;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PassiveSkillMap implements ModifierMap<PassiveSkill> {
    private final MMOPlayerData playerData;
    private final Map<UUID, PassiveSkill> skills = new HashMap<>();

    /**
     * Key: skill handler identifier
     * Value: last time that skill was cast due to a timer
     */
    private final Map<String, Long> lastCast = new HashMap<>();

    public PassiveSkillMap(MMOPlayerData playerData) {
        this.playerData = playerData;
    }

    public MMOPlayerData getPlayerData() {
        return playerData;
    }

    /**
     * Registers as active a skill trigger. It can be unregistered
     * later if necessary using {@link #removeModifiers(String)}.
     * From the time where that method is called, performing an action will
     * cause the saved PassiveSkill to be executed.
     *
     * @param skill Skill to register
     */
    @Override
    public void addModifier(PassiveSkill skill) {
        skills.put(skill.getUniqueId(), skill);
    }

    /**
     * Unregisters a skill with a specific identifier
     */
    @Override
    public void removeModifier(UUID uuid) {
        skills.remove(uuid);
    }

    /**
     * Unregisters active skill triggers with a specific key
     *
     * @param key Modifier key
     */
    @Override
    public void removeModifiers(String key) {
        Iterator<PassiveSkill> iter = skills.values().iterator();
        while (iter.hasNext()) {
            PassiveSkill skill = iter.next();
            if (skill.getKey().equals(key))
                iter.remove();
        }
    }

    @Override
    public Collection<PassiveSkill> getModifiers() {
        return skills.values();
    }

    /**
     * This method can be used to check if a player has a specific
     * passive skill registered in his skill set.
     * <p>
     * This is the method utilized to keep MythicLib compatible
     * with default ML passive skills like {@link Backstab}
     * <p>
     * A player can have multiple passive skills with the same
     * skill handler. The output function is completely random
     * given the use of an HashSet which does not feature order
     *
     * @param handler Some passive skill handler
     * @return Any passive skill with the same handler
     */
    @Nullable
    public PassiveSkill getSkill(@NotNull SkillHandler handler) {
        for (PassiveSkill passive : skills.values())
            if (handler.equals(passive.getTriggeredSkill().getHandler()))
                return passive;
        return null;
    }

    /**
     * Keep in mind this method is called 20 times a second for
     * every player in the server. It's pretty important to have
     * this method properly optimize and avoid things like useless
     * map checkups
     *
     * @author jules
     */
    public void tickTimerSkills() {

        // Do not initialize triggerMeta unless absolutely necessary
        TriggerMetadata triggerMeta = null;

        for (PassiveSkill passive : skills.values())
            if (passive.getType().equals(TriggerType.TIMER)) {
                String key = passive.getTriggeredSkill().getHandler().getId();
                final long lastCast = Objects.requireNonNullElse(this.lastCast.get(key), 0l); // Avoids one map checkup taking advantage of non null values
                if (lastCast + passive.getTimerPeriod() > System.currentTimeMillis())
                    continue;

                this.lastCast.put(key, System.currentTimeMillis());
                passive.getTriggeredSkill().cast(triggerMeta != null ? triggerMeta : (triggerMeta = new TriggerMetadata(getPlayerData().getStatMap().cache(EquipmentSlot.MAIN_HAND), null, null)));
            }
    }
}
