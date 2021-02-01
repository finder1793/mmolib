package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.DamageType;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.comp.MythicMobsHook;
import io.lumine.mythic.lib.gui.PluginInventory;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class AttackEffects implements Listener {
    private static final Random random = new Random();

    private double critCoefficient, spellCritCoefficient;

    public AttackEffects() {
        reload();
    }

    public void reload() {
        critCoefficient = MythicLib.plugin.getConfig().getDouble("crit-coefficient");
        spellCritCoefficient = MythicLib.plugin.getConfig().getDouble("spell-crit-coefficient");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void a(PlayerJoinEvent event) {
        MMOPlayerData.setup(event.getPlayer());
    }

    @EventHandler
    public void b(PlayerQuitEvent event) {
        MMOPlayerData.get(event.getPlayer()).setPlayer(null);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void c(PlayerAttackEvent event) {

        /*
         * damage modifiers. they do not stack geometrically (not 1.1 x 1.2 ...
         * but rather * (1 + ( 0.1 + 0.2 ... )). damage is multiplied after
         * adding up all the percent based damage stats
         */
        double d = 0;
        StatMap stats = event.getData().getStatMap();
        for (DamageType type : event.getAttack().getTypes())
            d += stats.getStat(type.getStat()) / 100;

        if (MythicLib.plugin.getVersion().getWrapper().isUndead(event.getEntity()))
            d += stats.getStat("UNDEAD_DAMAGE") / 100;

        if (MythicLib.plugin.hasMythicMobs()) {
            String faction = MythicMobsHook.getFaction(event.getEntity());
            if (faction != null)
                d += stats.getStat("FACTION_DAMAGE_" + faction.toUpperCase()) / 100;
        }

        d += stats.getStat(event.getEntity() instanceof Player ? "PVP_DAMAGE" : "PVE_DAMAGE") / 100;
        event.getAttack().multiplyDamage(1 + d);

        /*
         * weapon critical strikes
         */
        if (event.getAttack().hasType(DamageType.WEAPON) && random.nextDouble() <= stats.getStat("CRITICAL_STRIKE_CHANCE") / 100) {
            event.getAttack().multiplyDamage(critCoefficient + stats.getStat("CRITICAL_STRIKE_POWER") / 100);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 1);
            event.getEntity().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, event.getEntity().getLocation().add(0, 1, 0), 16, 0, 0, 0, .1);
        }

        /*
         * skill critical strikes
         */
        if (event.getAttack().hasType(DamageType.SKILL) && random.nextDouble() <= stats.getStat("SPELL_CRITICAL_STRIKE_CHANCE") / 100) {
            event.getAttack().multiplyDamage(spellCritCoefficient + stats.getStat("SPELL_CRITICAL_STRIKE_POWER") / 100);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 2);
            event.getEntity().getWorld().spawnParticle(Particle.TOTEM, event.getEntity().getLocation().add(0, 1, 0), 32, 0, 0, 0, .4);
        }
    }

    @EventHandler
    public void d(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof PluginInventory)
            ((PluginInventory) event.getInventory().getHolder()).whenClicked(event);
    }
}
