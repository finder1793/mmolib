package io.lumine.mythic.lib.api.event.fake;

import io.lumine.mythic.lib.comp.interaction.InteractionType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DamageCheckEvent extends FakeEntityDamageByEntityEvent {
    private final InteractionType interactionType;

    /**
     * This is the fake event used by MythicLib to
     * determine if a player can hit ANY entity
     *
     * @param damager Player damaging the entity
     * @param target  Entity being attacked
     */
    public DamageCheckEvent(@NotNull Player damager, @NotNull Entity target, @NotNull InteractionType interactionType) {
        super(damager, target, DamageCause.ENTITY_ATTACK, 0);

        this.interactionType = interactionType;
    }

    @NotNull
    public InteractionType getInteractionType() {
        return interactionType;
    }
}
