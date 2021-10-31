package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.mitigation.PlayerBlockEvent;
import io.lumine.mythic.lib.api.event.mitigation.PlayerDodgeEvent;
import io.lumine.mythic.lib.api.event.mitigation.PlayerParryEvent;
import io.lumine.mythic.lib.player.cooldown.CooldownType;
import io.lumine.mythic.lib.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.version.VersionSound;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MitigationMechanics implements Listener {
    private static final Random random = new Random();
    private static final List<EntityDamageEvent.DamageCause> mitigationCauses = Arrays.asList(EntityDamageEvent.DamageCause.PROJECTILE, EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK);
    private static final DecimalFormat digit = new DecimalFormat("0.#");

    // Mitigation configs
    private boolean dodgeKnockbackEnabled, parryKnockbackEnabled, actionBarMessage;
    private double dodgeKnockbackForce, maxDodgeRating, maxParryRating, maxBlockRating, maxBlockPower, defaultBlockPower, parryKnockbackForce;
    private double parryDefaultCooldown, blockDefaultCooldown, dodgeDefaultCooldown, parryMinCooldown, blockMinCooldown, dodgeMinCooldown;

    // Mitigation chat messages
    private String parryMessage, blockMessage, dodgeMessage;

    public MitigationMechanics() {
        reload();
    }

    public void reload() {
        dodgeKnockbackEnabled = MythicLib.plugin.getConfig().getBoolean("mitigation.dodge.knockback.enabled");
        dodgeKnockbackForce = MythicLib.plugin.getConfig().getDouble("mitigation.dodge.knockback.force");

        maxDodgeRating = MythicLib.plugin.getConfig().getDouble("mitigation.dodge.rating-max");
        maxParryRating = MythicLib.plugin.getConfig().getDouble("mitigation.parry.rating-max");
        maxBlockRating = MythicLib.plugin.getConfig().getDouble("mitigation.block.rating-max");
        maxBlockPower = MythicLib.plugin.getConfig().getDouble("mitigation.block.power.max");
        defaultBlockPower = MythicLib.plugin.getConfig().getDouble("mitigation.block.power.default");

        parryDefaultCooldown = MythicLib.plugin.getConfig().getDouble("mitigation.parry.cooldown.default");
        blockDefaultCooldown = MythicLib.plugin.getConfig().getDouble("mitigation.block.cooldown.default");
        dodgeDefaultCooldown = MythicLib.plugin.getConfig().getDouble("mitigation.dodge.cooldown.default");
        parryMinCooldown = MythicLib.plugin.getConfig().getDouble("mitigation.parry.cooldown.min");
        blockMinCooldown = MythicLib.plugin.getConfig().getDouble("mitigation.block.cooldown.min");
        dodgeMinCooldown = MythicLib.plugin.getConfig().getDouble("mitigation.dodge.cooldown.min");

        parryKnockbackEnabled = MythicLib.plugin.getConfig().getBoolean("mitigation.parry.knockback.enabled");
        parryKnockbackForce = MythicLib.plugin.getConfig().getDouble("mitigation.parry.knockback.force");

        parryMessage = MythicLib.plugin.getConfig().getString("mitigation.message.parry");
        dodgeMessage = MythicLib.plugin.getConfig().getString("mitigation.message.dodge");
        blockMessage = MythicLib.plugin.getConfig().getString("mitigation.message.block");
        actionBarMessage = MythicLib.plugin.getConfig().getBoolean("mitigation.message.action-bar");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void a(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || !mitigationCauses.contains(event.getCause()) || event.getEntity().hasMetadata("NPC"))
            return;

        Player player = (Player) event.getEntity();
        MMOPlayerData playerData = MMOPlayerData.get(player);
        StatMap stats = playerData.getStatMap();

        // Dodging
        double dodgeRating = Math.min(stats.getStat("DODGE_RATING"), maxDodgeRating) / 100;
        if (random.nextDouble() < dodgeRating && !playerData.isOnCooldown(CooldownType.DODGE)) {

            PlayerDodgeEvent mitigationEvent = new PlayerDodgeEvent(playerData, event);
            Bukkit.getPluginManager().callEvent(mitigationEvent);
            if (mitigationEvent.isCancelled())
                return;

            sendMessage(player, dodgeMessage, "damage", digit.format(event.getFinalDamage()));
            playerData.applyCooldown(CooldownType.DODGE, calculateCooldown(dodgeDefaultCooldown, stats.getStat("DODGE_COOLDOWN_REDUCTION") / 100, dodgeMinCooldown));
            event.setCancelled(true);
            player.getWorld().playSound(player.getLocation(), VersionSound.ENTITY_ENDER_DRAGON_FLAP.toSound(), 2, 1);
            player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 16, 0, 0, 0, .06);
            if (dodgeKnockbackEnabled)
                player.setVelocity(getVector(player, event).multiply(.85 * dodgeKnockbackForce).setY(.3));
            return;
        }

        // Parrying
        double parryRating = Math.min(stats.getStat("PARRY_RATING"), maxParryRating) / 100;
        if (random.nextDouble() < parryRating && !playerData.isOnCooldown(CooldownType.PARRY)) {

            PlayerParryEvent mitigationEvent = new PlayerParryEvent(playerData, event);
            Bukkit.getPluginManager().callEvent(mitigationEvent);
            if (mitigationEvent.isCancelled())
                return;

            playerData.applyCooldown(CooldownType.PARRY, calculateCooldown(parryDefaultCooldown, stats.getStat("PARRY_COOLDOWN_REDUCTION") / 100, parryMinCooldown));
            event.setCancelled(true);
            sendMessage(player, parryMessage, "damage", digit.format(event.getFinalDamage()));
            player.getWorld().playSound(player.getLocation(), VersionSound.ENTITY_ENDER_DRAGON_FLAP.toSound(), 2, 1);
            player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 16, 0, 0, 0, .06);
            if (parryKnockbackEnabled && event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) ((EntityDamageByEntityEvent) event).getDamager();
                attacker.setVelocity(normalize(attacker.getLocation().toVector().subtract(player.getLocation().toVector())).setY(.35).multiply(parryKnockbackForce));
            }
            return;
        }

        // Blocking
        double blockRating = Math.min(stats.getStat("BLOCK_RATING"), maxBlockRating) / 100;
        if (random.nextDouble() < blockRating && !playerData.isOnCooldown(CooldownType.BLOCK)) {

            double blockPower = Math.min(defaultBlockPower + stats.getStat("BLOCK_POWER"), maxBlockPower) / 100;
            PlayerBlockEvent mitigationEvent = new PlayerBlockEvent(playerData, event, blockPower);
            Bukkit.getPluginManager().callEvent(mitigationEvent);
            if (mitigationEvent.isCancelled())
                return;

            playerData.applyCooldown(CooldownType.BLOCK, calculateCooldown(blockDefaultCooldown, stats.getStat("BLOCK_COOLDOWN_REDUCTION") / 100, blockMinCooldown));
            sendMessage(player, blockMessage, "damage", digit.format(mitigationEvent.getDamageBlocked()), "power", digit.format(mitigationEvent.getPower() * 100.));
            event.setDamage(event.getDamage() * (1 - mitigationEvent.getPower()));
            player.getWorld().playSound(player.getLocation(), VersionSound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR.toSound(), 2, 1);

            double yaw = getYaw(player, getVector(player, event)) - 90;
            for (double j = yaw - 90; j < yaw + 90; j += 5)
                for (double y = 0; y < 2; y += .1)
                    player.getWorld().spawnParticle(Particle.REDSTONE,
                            player.getLocation().clone().add(Math.cos(Math.toRadians(j)) * .7, y, Math.sin(Math.toRadians(j)) * .7), 1,
                            new Particle.DustOptions(Color.GRAY, 1f));
        }
    }

    private void sendMessage(Player player, String format, Object... placeholders) {
        if (format == null || format.isEmpty())
            return;

        format = ChatColor.translateAlternateColorCodes('&', format);
        for (int i = 0; i < placeholders.length; i += 2)
            format = format.replace("#" + placeholders[i].toString() + "#", placeholders[i + 1].toString());

        if (actionBarMessage)
            MythicLib.plugin.getVersion().getWrapper().sendActionBar(player, format);
        else
            player.sendMessage(format);
    }

    /**
     * @param cooldown  Cooldown by defaut
     * @param reduction Mitigation cooldown reduction
     * @param min       Minimum cooldown
     * @return The actual player cooldown
     */
    private double calculateCooldown(double cooldown, double reduction, double min) {
        return Math.max(min, cooldown * (1 - reduction));
    }

    private Vector getVector(Player player, EntityDamageEvent event) {
        return event instanceof EntityDamageByEntityEvent ? normalize(player.getLocation().subtract(((EntityDamageByEntityEvent) event).getDamager().getLocation()).toVector()) : player.getEyeLocation().getDirection();
    }

    private Vector normalize(Vector vec) {
        return vec.lengthSquared() == 0 ? vec : vec.normalize();
    }

    private double getYaw(Entity player, Vector vec) {
        return new Location(player.getWorld(), vec.getX(), vec.getY(), vec.getZ()).setDirection(vec).getYaw();
    }
}

