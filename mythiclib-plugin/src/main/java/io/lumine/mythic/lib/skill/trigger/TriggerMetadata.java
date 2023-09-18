package io.lumine.mythic.lib.skill.trigger;

import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.player.PlayerMetadata;
import io.lumine.mythic.lib.script.variable.VariableList;
import io.lumine.mythic.lib.script.variable.VariableScope;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.SkillMetadata;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Contains sufficient information in order to trigger either
 * one or multiple skills. This class contains a few redundancies
 * including the MMO player data and the action hand, the main
 * reason being that the {@link #caster} field is nullable.
 *
 * @author jules
 */
public class TriggerMetadata {

    private final MMOPlayerData playerData;
    private final TriggerType triggerType;
    private final EquipmentSlot actionHand;

    @Nullable
    private final Entity target;

    @Nullable
    private final AttackMetadata attack;

    /**
     * The instanciation of a PlayerMetadata can be quite intensive in
     * computation, especially because it can be up to 20 times a second
     * for every player in the server. It performs a full stat map lookup
     * and caches final stat values.
     * <p>
     * For this reason, it's best to NOT generate the PlayerMetadata unless
     * it has been provided beforehand in the constructor, until it's finally
     * asked for in the getter.
     */
    @Nullable
    private PlayerMetadata caster;

    public TriggerMetadata(@NotNull MMOPlayerData caster, @NotNull TriggerType triggerType) {
        this(caster, triggerType, null);
    }

    public TriggerMetadata(@NotNull MMOPlayerData caster, @NotNull TriggerType triggerType, @Nullable Entity target) {
        this(caster, triggerType, EquipmentSlot.MAIN_HAND, target, null, null);
    }

    /**
     * The player responsible for the attack is the one triggering the skill.
     */
    public TriggerMetadata(@NotNull PlayerAttackEvent attackEvent, @NotNull TriggerType triggerType) {
        this(attackEvent.getAttacker(), triggerType, attackEvent.getEntity(), attackEvent.getAttack());
    }

    public TriggerMetadata(@NotNull PlayerMetadata caster, @NotNull TriggerType triggerType, @Nullable Entity target, @Nullable AttackMetadata attack) {
        this(caster.getData(), triggerType, caster.getActionHand(), target, attack, caster);
    }

    public TriggerMetadata(@NotNull MMOPlayerData playerData, @NotNull TriggerType triggerType, @Nullable EquipmentSlot actionHand, @Nullable Entity target, @Nullable AttackMetadata attack, @Nullable PlayerMetadata caster) {
        this.playerData = Objects.requireNonNull(playerData);
        this.triggerType = Objects.requireNonNull(triggerType);
        this.actionHand = Objects.requireNonNullElse(actionHand, EquipmentSlot.MAIN_HAND);
        this.target = target;
        this.attack = attack;
        this.caster = caster;
    }

    @NotNull
    public MMOPlayerData getPlayerData() {
        return playerData;
    }

    @NotNull
    public TriggerType getTriggerType() {
        return triggerType;
    }

    @NotNull
    public EquipmentSlot getActionHand() {
        return actionHand;
    }

    @Nullable
    public Entity getTarget() {
        return target;
    }

    @Nullable
    public AttackMetadata getAttack() {
        return attack;
    }

    @NotNull
    public PlayerMetadata getCachedPlayerMetadata() {
        if (caster == null) caster = playerData.getStatMap().cache(actionHand);
        return caster;
    }

    /**
     * Called when casting a skill. This transfers all the useful
     * information into a skill metadata
     *
     * @param cast Skill being cast
     * @return Skill cast information containing all the previous information
     */
    @NotNull
    public SkillMetadata toSkillMetadata(Skill cast) {
        return new SkillMetadata(cast, getCachedPlayerMetadata(), new VariableList(VariableScope.SKILL), getCachedPlayerMetadata().getPlayer().getLocation(), null, target, null, attack);
    }
}
