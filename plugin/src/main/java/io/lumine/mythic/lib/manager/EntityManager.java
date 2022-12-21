package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.comp.target.TargetRestriction;
import io.lumine.mythic.lib.util.CustomProjectile;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityManager {
    private final Set<TargetRestriction> restrictions = new HashSet<>();
    private final Map<Integer, CustomProjectile> projectiles = new HashMap<>();

    /**
     * See {@link TargetRestriction} for more information. This should be
     * called as soon as MythicLib enablesby plugins implementing player sets
     * like parties, friends, factions.... any set that could support friendly fire.
     * <p>
     * This is also helpful to prevent players from interacting with
     * specific invulnerable entities like NPCs
     *
     * @param restriction New restriction for entities
     */
    public void registerRestriction(TargetRestriction restriction) {
        restrictions.add(restriction);
    }

    @Nullable
    public CustomProjectile getCustomProjectile(Entity entity) {
        return projectiles.get(entity.getEntityId());
    }

    /**
     * Registers a custom projectile. This is used for bows, crossbows and tridents.
     */
    public void registerCustomProjectile(Entity entity, CustomProjectile projectileData) {
        projectiles.put(entity.getEntityId(), projectileData);
    }

    /**
     * Called whenever a player tries to damage OR buff an entity.
     * <p>
     * This should be used by:
     * - plugins which implement friendly fire player sets like parties, guilds, nations, factions....
     * - plugins which implement custom invulnerable entities like NPCs, sentinels....
     *
     * @param source      Player targeting another entity
     * @param target      Entity being targeted
     * @param interaction Type of interaction, whether it's positive (buff, heal) or negative (offense skill, attack)
     * @return If false, any interaction should be cancelled!
     */
    public boolean canTarget(@NotNull Player source, @NotNull Entity target, @NotNull InteractionType interaction) {

        // Simple checks
        if (source.equals(target) || target.isDead() || !(target instanceof LivingEntity) || target instanceof ArmorStand)
            return false;

        // PvP checks
        if (interaction.isOffense() && target instanceof Player &&
                (!target.getWorld().getPVP() || (interaction == InteractionType.OFFENSE_SKILL && !MythicLib.plugin.getMMOConfig().playerAbilityDamage) || !MythicLib.plugin.getFlags().isPvpAllowed(target.getLocation())))
            return false;

        // Specific plugin checks (Citizens, Factions..)
        final LivingEntity livingTarget = (LivingEntity) target;
        for (TargetRestriction restriction : restrictions)
            if (!restriction.canTarget(source, livingTarget, interaction))
                return false;

        return true;
    }

    public void unregisterCustomProjectile(Projectile projectile) {
        projectiles.remove(projectile.getEntityId());
    }
}
