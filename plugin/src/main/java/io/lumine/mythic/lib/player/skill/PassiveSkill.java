package io.lumine.mythic.lib.player.skill;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.PlayerModifier;
import io.lumine.mythic.lib.skill.SimpleSkill;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * There is one PassiveSkill instance per passive skill the player has.
 * A passive skill can be registered by MMOItems items or MMOCore passive skills.
 * <p>
 * The distinction between active and passive skills is pretty vague.
 * <p>
 * In MMOItems:
 * we consider a skill that is cast when right clicking active (for
 * instance when holding an item) even though it's handled just like
 * an on-hit passive skill within MythicLib. It seems pretty confusing.
 * <p>
 * It's better to consider ANY MMOITEMS SKILL to be passive and
 * distinguish silent from non-silent trigger types.
 * See {@link TriggerType#isSilent()}
 * <p>
 * In MMOCore:
 * Skills that are cast in MMOCore using the casting mode are also
 * active and any skill that has to be triggered is passive. It's
 * much less confusing in that context
 * <p>
 * Conclusion:
 * The only active skills are the ones cast using MMOCore casting mode
 * i.e using {@link TriggerType#CAST}; any other skill is passive.
 *
 * @author indyuce
 */
public class PassiveSkill extends PlayerModifier {

    /**
     * Skill cast whenever the action is performed
     */
    private final Skill triggered;

    /**
     * Zero when skill does not use
     */
    private final long timerPeriod;

    @Deprecated
    public PassiveSkill(String key, TriggerType type, Skill triggered, EquipmentSlot equipmentSlot, ModifierSource modifierSource) {
        this(key, triggered, equipmentSlot, modifierSource);
    }

    /**
     * @param key            A key like 'item' or 'itemSet' indicating what is giving a triggered skill to the player.
     *                       There can be multiple skills with the same key, it's not a unique identifier.
     *                       It can be later used to isolate and unregister skills with a certain key.
     * @param triggered      The skill
     * @param equipmentSlot  The equipment slot granting this passive skill
     * @param modifierSource The source of the passive skill
     */
    public PassiveSkill(String key, Skill triggered, EquipmentSlot equipmentSlot, ModifierSource modifierSource) {
        super(key, equipmentSlot, modifierSource);

        Validate.isTrue(triggered.getTrigger().isPassive(), "Skill is active");
        this.triggered = Objects.requireNonNull(triggered, "Skill cannot be null");
        this.timerPeriod = Math.max(1, (long) triggered.getModifier("timer"));
    }

    @Deprecated
    public PassiveSkill(String key, TriggerType type, Skill triggered) {
        this(key, triggered, EquipmentSlot.OTHER, ModifierSource.OTHER);
    }

    public PassiveSkill(ConfigObject obj) {
        super(obj.getString("key"), EquipmentSlot.OTHER, ModifierSource.OTHER);

        triggered = new SimpleSkill(TriggerType.API, MythicLib.plugin.getSkills().getHandlerOrThrow(obj.getString("skill")));
        timerPeriod = 0;
    }

    @NotNull
    public Skill getTriggeredSkill() {
        return triggered;
    }

    public long getTimerPeriod() {
        return timerPeriod;
    }

    @NotNull
    public TriggerType getType() {
        return triggered.getTrigger();
    }

    @Override
    public void register(MMOPlayerData playerData) {
        playerData.getPassiveSkillMap().addModifier(this);
    }

    @Override
    public void unregister(MMOPlayerData playerData) {
        playerData.getPassiveSkillMap().removeModifier(getUniqueId());
    }
}
