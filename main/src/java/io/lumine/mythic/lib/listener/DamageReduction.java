package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.AttackResult;
import io.lumine.mythic.lib.api.DamageType;
import io.lumine.mythic.lib.api.RegisteredAttack;
import io.lumine.mythic.lib.api.math.EvaluatedFormula;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.function.BiFunction;
import java.util.logging.Level;

public class DamageReduction implements Listener {

    // Since mythic mobs is a soft depend, this event triggers
    // correctly, fixing a bug with mm skill mechanics.
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void a(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getEntity().hasMetadata("NPC"))
            return;

        MMOPlayerData data = MMOPlayerData.get((OfflinePlayer) event.getEntity());
        RegisteredAttack attack = MythicLib.plugin.getDamage().findInfo(event.getEntity());
        AttackResult result = attack == null ? new AttackResult(true, DamageType.WEAPON, DamageType.PHYSICAL) : attack.getResult();

        double damage = event.getDamage();

        /**
         * Applies damage reduction due to the DEFENSE stat
         */
        double defense = data.getStatMap().getStat("DEFENSE");
        damage = defense > 0 ? new DefenseFormula(defense).getAppliedDamage(event.getDamage()) : event.getDamage();

        /**
         * Applies damage reduction due to damage reductison stats, like
         * MAGIC_DAMAGE_REDUCTION or simply DAMAGE_REDUCTION
         */
        double afterMitigation = 1;
        for (DamageReductionType type : DamageReductionType.values())
            if (type.isApplicable(result, event))
                afterMitigation *= 1 - Math.min(1, data.getStatMap().getStat(type.getStat()) / 100);

        /**
         * Finally update the event damage output.
         */
        event.setDamage(damage * afterMitigation);
    }

    /**
     * Used for calculating damage mitigation due to the defense stat.
     */
    public class DefenseFormula {
        private final double defense;

        public DefenseFormula(double defense) {
            this.defense = defense;
        }

        public double getAppliedDamage(double damage) {
            String formula = MythicLib.plugin.getConfig().getString("defense-application", "#damage# * (1 - (#defense# / (#defense# + 100)))");
            formula = formula.replace("#defense#", String.valueOf(defense));
            formula = formula.replace("#damage#", String.valueOf(damage));

            try {
                return Math.max(0, new EvaluatedFormula(formula).evaluate());
            } catch (RuntimeException exception) {

                /**
                 * Formula won't evaluate if hanging #'s or unparsed placeholders. Send a
                 * friendly warning to console and just return the default damage.
                 */
                MythicLib.inst().getLogger()
                        .log(Level.WARNING, "Could not evaluate defense formula, please check config.");
                return damage;
            }
        }
    }

    /**
     * All different types of damage reduction.
     */
    public enum DamageReductionType {

        /*
         * Damage reduction, always applies
         */
        ENVIRONMENTAL("DAMAGE_REDUCTION", (result, event) -> true),

        /*
         * Fight based damage reduction types
         */
        PVP((result, event) -> event instanceof EntityDamageByEntityEvent && getDamager((EntityDamageByEntityEvent) event) instanceof Player),
        PVE((result, event) -> event instanceof EntityDamageByEntityEvent && !(getDamager((EntityDamageByEntityEvent) event) instanceof Player)),

        /*
         * Simple damage reduction types
         */
        FIRE((result, event) -> event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK),
        FALL((result, event) -> event.getCause() == EntityDamageEvent.DamageCause.FALL),

        /*
         * Damage type based damage reduction types
         */
        MAGIC((result, event) -> event.getCause() == EntityDamageEvent.DamageCause.MAGIC || (result != null && result.hasType(DamageType.MAGIC))),
        PHYSICAL((result, event) -> event instanceof EntityDamageByEntityEvent || (result != null && result.hasType(DamageType.PHYSICAL))),
        WEAPON((result, event) -> result != null && result.hasType(DamageType.WEAPON)),
        SKILL((result, event) -> result != null && result.hasType(DamageType.SKILL)),
        PROJECTILE((result,
                    event) -> (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Projectile)
                || (result != null && result.hasType(DamageType.PROJECTILE)));

        /**
         * The corresponding item stat that will be used to
         * apply damage reduction. For instance, ENVIRONMENTAL calls
         * the DAMAGE_REDUCTION_STAT and MAGIC calls MAGIC_DAMAGE_REDUCTION
         */
        private final String stat;

        /**
         * Whether or not the corresponding stat {@link DamageReductionType#stat}
         * should apply in a given attack, ie AttackResult and EntityDamageEvent
         */
        private final BiFunction<AttackResult, EntityDamageEvent, Boolean> apply;

        private DamageReductionType(String stat, BiFunction<AttackResult, EntityDamageEvent, Boolean> apply) {
            this.stat = stat;
            this.apply = apply;
        }

        private DamageReductionType(BiFunction<AttackResult, EntityDamageEvent, Boolean> apply) {
            this.stat = name() + "_DAMAGE_REDUCTION";
            this.apply = apply;
        }

        public boolean isApplicable(AttackResult result, EntityDamageEvent event) {
            return apply.apply(result, event);
        }

        public String getStat() {
            return stat;
        }
    }

    /**
     * Tries to find the entity who dealt the damage in some attack event. Main issue is that
     * if it is a ranged attack like a trident or an arrow, we have to find back the shooter.
     */
    private static LivingEntity getDamager(EntityDamageByEntityEvent event) {

        /*
         * Check direct damager
         */
        if (event.getDamager() instanceof LivingEntity)
            return (LivingEntity) event.getDamager();

        /*
         * Checks projectile and add damage type, which supports every vanilla
         * projectile like snowballs, tridents and arrows
         */
        if (event.getDamager() instanceof Projectile) {
            Projectile proj = (Projectile) event.getDamager();
            if (proj.getShooter() instanceof LivingEntity)
                return (LivingEntity) proj.getShooter();
        }

        return null;
    }
}
