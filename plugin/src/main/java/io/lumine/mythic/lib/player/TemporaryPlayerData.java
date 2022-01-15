package io.lumine.mythic.lib.player;

import io.lumine.mythic.lib.api.event.TemporaryDataSavedEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.player.cooldown.CooldownMap;
import io.lumine.mythic.lib.player.particle.ParticleEffectMap;
import io.lumine.mythic.lib.player.potion.PermanentPotionEffectMap;
import io.lumine.mythic.lib.player.skill.PassiveSkillMap;
import io.lumine.mythic.lib.skill.custom.variable.VariableList;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This object includes all the player data that changes with
 * time and that does not deserve to be saved in a database as
 * you know what state it takes if you wait long enough..
 * <p>
 * This includes
 * - player cooldowns (handled by MythicLib)
 * - mana/stamina/stellium (MMOCore)
 * - stat modifiers (MythicLib)
 * <p>
 * This data is cached when a player logs off for a specific
 * amount of time. If the player doesn't reconnect after X hours
 * then this data gets completely garbage collected.
 * <p>
 * Linked classes
 * - {@link TemporaryDataSavedEvent}
 *
 * @author jules
 */
public class TemporaryPlayerData {
    private final CooldownMap cooldownMap;
    private final StatMap statMap;
    private final PermanentPotionEffectMap permEffectMap;
    private final VariableList skillVariableList;
    private final ParticleEffectMap particleEffectMap;
    private final PassiveSkillMap passiveSkills;
    private final long lastDisconnection;

    /**
     * Cache time out is set to one full day. If the player does NOT reconnect
     * after one day, the temporary player data will be completely lost.
     */
    private static final long CACHE_TIME_OUT = 1000 * 60 * 60 * 24;

    /**
     * Map used by other plugins to save any type of data
     */
    private final Map<String, Object> data = new HashMap<>();

    private static final Map<UUID, TemporaryPlayerData> temporaryData = new HashMap<>();

    public TemporaryPlayerData(MMOPlayerData loggingOff) {
        this.cooldownMap = loggingOff.getCooldownMap();
        this.statMap = loggingOff.getStatMap();
        this.permEffectMap = loggingOff.getPermanentEffectMap();
        particleEffectMap = loggingOff.getParticleEffectMap();
        this.skillVariableList = loggingOff.getVariableList();
        this.passiveSkills = loggingOff.getPassiveSkillMap();
        this.lastDisconnection = System.currentTimeMillis();
    }

    public boolean isTimedOut() {
        return lastDisconnection + CACHE_TIME_OUT < System.currentTimeMillis();
    }

    public CooldownMap getCooldownMap() {
        return cooldownMap;
    }

    public StatMap getStatMap() {
        return statMap;
    }

    public PermanentPotionEffectMap getPermanentEffectMap() {
        return permEffectMap;
    }

    public ParticleEffectMap getParticleEffectMap() {
        return particleEffectMap;
    }

    public VariableList getSkillVariableList() {
        return skillVariableList;
    }

    public PassiveSkillMap getPassiveSkills() {
        return passiveSkills;
    }

    public Set<String> getDataKeys() {
        return data.keySet();
    }

    public String getString(String key) {
        return data.get(key).toString();
    }

    public double getDouble(String key) {
        return (double) data.get(key);
    }

    public void set(String key, Object obj) {
        data.put(key, obj);
    }

    /**
     * Unloads all time out temporary player data. This should be checked
     * once every hour or so by MythicLib to make sure not to overheat memory.
     */
    public static void flush() {
        Iterator<TemporaryPlayerData> iterator = temporaryData.values().iterator();
        while (iterator.hasNext()) {
            TemporaryPlayerData tempData = iterator.next();
            if (tempData.isTimedOut())
                iterator.remove();
        }
    }

    public static boolean has(Player player) {
        return has(player.getUniqueId());
    }

    public static boolean has(UUID uuid) {
        return temporaryData.containsKey(uuid);
    }

    public static TemporaryPlayerData get(Player player) {
        return get(player.getUniqueId());
    }

    public static TemporaryPlayerData get(UUID uuid) {
        return Objects.requireNonNull(temporaryData.get(uuid), "Temporary player data not loaded");
    }

    public static void load(Player player, TemporaryPlayerData tempData) {
        load(player.getUniqueId(), tempData);
    }

    public static void load(UUID uuid, TemporaryPlayerData tempData) {
        temporaryData.put(uuid, tempData);
    }

    public static void unload(Player player) {
        unload(player.getUniqueId());
    }

    public static void unload(UUID uuid) {
        temporaryData.remove(uuid);
    }
}