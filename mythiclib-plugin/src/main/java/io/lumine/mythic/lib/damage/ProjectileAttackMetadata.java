package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.api.stat.provider.StatProvider;
import io.lumine.mythic.lib.player.PlayerMetadata;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

/**
 * Used by attacks caused by projectiles like ranged skills
 * or weapon attacks with bows, crossbows or tridents.
 */
public class ProjectileAttackMetadata extends AttackMetadata {
    private final Projectile projectile;

    @Deprecated
    public ProjectileAttackMetadata(DamageMetadata damage, PlayerMetadata attacker, Projectile projectile) {
        this(damage, null, attacker, projectile);
    }

    /**
     * Used by AttackHandler instances to register attacks. AttackResult only
     * gives information about the attack damage and types while this class also
     * contains info about the damager. Some plugins don't let MythicLib determine
     * what the damager is so there might be problem with damage/reduction stat
     * application.
     *
     * @param damage   The attack result
     * @param attacker The entity who dealt the damage
     */
    public ProjectileAttackMetadata(DamageMetadata damage, LivingEntity target, StatProvider attacker, Projectile projectile) {
        super(damage, target, attacker);

        this.projectile = projectile;
    }

    /**
     * @return Projectile which hit the entity
     */
    public Projectile getProjectile() {
        return projectile;
    }
}
