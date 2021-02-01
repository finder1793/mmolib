package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.api.AttackResult;
import io.lumine.mythic.lib.api.DamageHandler;
import io.lumine.mythic.lib.api.RegisteredAttack;
import org.apache.commons.lang.Validate;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;
import java.util.logging.Level;

public class DamageManager implements Listener, DamageHandler {
    private final Map<Integer, RegisteredAttack> customDamage = new HashMap<>();
    private final List<DamageHandler> handlers = new ArrayList<>();

    private static final AttributeModifier noKnockback = new AttributeModifier(UUID.randomUUID(), "noKnockback", 100, Operation.ADD_NUMBER);

    public DamageManager() {
        handlers.add(this);
    }

    /**
     * Damage handlers are used by MMOLib to keep track of details of every
     * attack so that it can apply damage based stats like PvE damage, Magic
     * Damage...
     *
     * @param handler Damage handler being registered
     */
    public void registerHandler(DamageHandler handler) {
        Validate.notNull(handler, "Damage handler cannot be null");
        handlers.add(handler);
    }

    @Override
    public boolean hasDamage(Entity entity) {
        return customDamage.containsKey(entity.getEntityId());
    }

    @Override
    public RegisteredAttack getDamage(Entity entity) {
        return customDamage.get(entity.getEntityId());
    }

    /**
     * Forces a player to damage an entity with knockback
     *
     * @param player The player damaging the entity
     * @param target The entity being damaged
     * @param result Info about the attack. Since this attack is registered as
     *               MMOLib damage, we need more than just a double for the atk
     *               damage
     */
    public void damage(Player player, LivingEntity target, AttackResult result) {
        damage(player, target, result, true);
    }

    /**
     * Forces a player to damage an entity with (no) knockback
     *
     * @param player    The player damaging the entity
     * @param target    The entity being damaged
     * @param result    Info about the attack. Since this attack is registered
     *                  as MMOLib damage, we need more than just a double for
     *                  the atk damage
     * @param knockback If the attack should deal knockback
     */
    public void damage(Player player, LivingEntity target, AttackResult result, boolean knockback) {
        if (target.hasMetadata("NPC") || player.hasMetadata("NPC"))
            return;

        customDamage.put(target.getEntityId(), new RegisteredAttack(result, player));

        if (!knockback)
            try {
                target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).addModifier(noKnockback);
                target.damage(result.getDamage(), player);
            } catch (Exception anyError) {
                MythicLib.plugin.getLogger().log(Level.WARNING, "An error occured while registering player damage");
                anyError.printStackTrace();
            } finally {
                target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).removeModifier(noKnockback);
            }

        else
            target.damage(result.getDamage(), player);
    }

    /**
     * @param  entity The entity to check
     * @return        Null if the entity is being damaged through vanilla
     *                actions, or the corresponding RegisteredAttack if MMOLib
     *                found a plugin responsible for that damage
     */
    public RegisteredAttack findInfo(Entity entity) {
        for (DamageHandler handler : handlers)
            if (handler.hasDamage(entity))
                return handler.getDamage(entity);
        return null;
    }

    /**
     * This method is used to unregister MMOLib custom damage after everything
     * was calculated, hence MONITOR priority
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void unregisterCustomDamage(EntityDamageByEntityEvent event) {
        customDamage.remove(event.getEntity().getEntityId());
    }
}
