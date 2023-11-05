package io.lumine.mythic.lib.api.event.fake;

import com.google.common.base.Function;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class FakeEntityDamageByEntityEvent extends EntityDamageByEntityEvent {
    public FakeEntityDamageByEntityEvent(@NotNull Entity damager, @NotNull Entity damagee, @NotNull EntityDamageEvent.DamageCause cause, double damage) {
        super(damager, damagee, cause, damage);
    }

    public FakeEntityDamageByEntityEvent(@NotNull Entity damager, @NotNull Entity damagee, @NotNull EntityDamageEvent.DamageCause cause, @NotNull Map<DamageModifier, Double> modifiers, @NotNull Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damager, damagee, cause, modifiers, modifierFunctions);
    }
}
