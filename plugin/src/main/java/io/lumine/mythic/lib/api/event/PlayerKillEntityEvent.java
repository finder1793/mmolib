package io.lumine.mythic.lib.api.event;

import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.player.PlayerMetadata;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerKillEntityEvent extends MMOPlayerDataEvent {
    private static final HandlerList handlers = new HandlerList();

    private final LivingEntity target;
    private final AttackMetadata attack;

    public PlayerKillEntityEvent(@NotNull AttackMetadata attack, @NotNull LivingEntity target) {
        super(((PlayerMetadata) attack.getAttacker()).getData());

        this.attack = attack;
        this.target = target;
    }

    @NotNull
    public LivingEntity getTarget() {
        return target;
    }

    @NotNull
    public AttackMetadata getAttack() {
        return attack;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
