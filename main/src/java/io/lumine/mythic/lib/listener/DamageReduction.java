package io.lumine.mythic.lib.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageReduction implements Listener {
    // Since mythic mobs is a soft depend, this event triggers
    // correctly, fixing a bug with mm skill mechanics.
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void a(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getEntity().hasMetadata("NPC"))
            return;

        MMOPlayerData data = MMOPlayerData.get((OfflinePlayer) event.getEntity());
        RegisteredAttack attack = MMOLib.plugin.getDamage().findInfo(event.getEntity());
        AttackResult result = attack == null ? new AttackResult(true, DamageType.WEAPON, DamageType.PHYSICAL) : attack.getResult();

        /*
         * Applies defense based damage reduction
         */
        double defense = data.getStatMap().getStat("DEFENSE");
        double damage = defense > 0 ? new DefenseCalculator(defense).getAppliedDamage(event.getDamage()) : event.getDamage();

        /*
         * Applies other damage reduction sources
         */
        DamageReductionCalculator reductionCalculator = new DamageReductionCalculator(data);
        for (DamageReductionType type : DamageReductionType.values())
            if (type.isApplicable(result, event))
                reductionCalculator.applyReduction(type);

        /*
         * Finally applies damage
         */
        event.setDamage(damage * reductionCalculator.getCoefficient());
    }

    /**
     * Used for calculating defense stats. Double outputs are given with up to 2
     * decimal places
     */
    public static class DefenseCalculator {
        private final double defense;

        public DefenseCalculator(double defense) {
            this.defense = defense;
        }

        /**
         * @return Percentage of damage reduced. Basically calculates the damage
         *         reduction for a 100 damage attack and makes it a %
         */
        @Deprecated
        public double getReductionPercent() {
            return truncation(100 - getAppliedDamage(100), 2);
        }

        public double getAppliedDamage(double damage) {
            String formula = MMOLib.plugin.getConfig().getString("defense-application", "#damage# * (1 - (#defense# / (#defense# + 100)))");
            formula = formula.replace("#defense#", String.valueOf(defense));
            formula = formula.replace("#damage#", String.valueOf(damage));
            // Doesn't run the formula if there are hanging #'s
            // or unparsed placeholders.
            try {
                return Math.max(0, new EvaluatedFormula(formula).evaluate());
            } catch (RuntimeException e) {
                return damage;
            }

        }

        private double truncation(double d, int places) {
            double p = Math.pow(10, places);
            return Math.floor(d * p) / p;
        }
    }

    /**
     * Util class to help and easily manage damage reduction formulas. currently
     * %'s do not add up which means 30% + 30% is not 60% but 51% to prevent OP
     * damage reduction stats.
     */
    public static class DamageReductionCalculator {
        private final StatMap stats;

        private double c = 1;

        public DamageReductionCalculator(MMOPlayerData data) {
            this.stats = data.getStatMap();
        }

        public void applyReduction(DamageReductionType type) {
            c *= 1 - Math.min(1, stats.getStat(type.getStat()) / 100);
        }

        double getCoefficient() {
            return c;
        }
    }

    /**
     * All different types of damage reduction are listed here
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
        FIRE((result, event) -> event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK),
        FALL((result, event) -> event.getCause() == DamageCause.FALL),

        /*
         * Damage type based damage reduction types
         */
        MAGIC((result, event) -> event.getCause() == DamageCause.MAGIC || (result != null && result.hasType(DamageType.MAGIC))),
        PHYSICAL((result, event) -> event instanceof EntityDamageByEntityEvent || (result != null && result.hasType(DamageType.PHYSICAL))),
        WEAPON((result, event) -> result != null && result.hasType(DamageType.WEAPON)),
        SKILL((result, event) -> result != null && result.hasType(DamageType.SKILL)),
        PROJECTILE((result,
                    event) -> (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Projectile)
                || (result != null && result.hasType(DamageType.PROJECTILE)));

        private final String stat;
        private final BiFunction<AttackResult, EntityDamageEvent, Boolean> apply;

        DamageReductionType(String stat, BiFunction<AttackResult, EntityDamageEvent, Boolean> apply) {
            this.stat = stat;
            this.apply = apply;
        }

        DamageReductionType(BiFunction<AttackResult, EntityDamageEvent, Boolean> apply) {
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
