package io.lumine.mythic.lib.comp.mythicmobs;

import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.player.CooldownInfo;
import io.lumine.mythic.lib.player.CooldownMap;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.Placeholder;
import io.lumine.xikage.mythicmobs.skills.variables.Variable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MythicMobsHook implements Listener {
    public MythicMobsHook() {
        registerPlaceholders();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void a(PlayerAttackEvent event) {

        // Apply MythicMobs faction damage
        String faction = getFaction(event.getEntity());
        if (faction != null)
            event.getDamage().multiply(1 + event.getData().getStatMap().getStat("FACTION_DAMAGE_" + faction.toUpperCase()) / 100);
    }

    @EventHandler
    public void b(MythicMechanicLoadEvent event) {
        if (event.getMechanicName().equalsIgnoreCase("mmodamage") || event.getMechanicName().equalsIgnoreCase("mmod"))
            event.register(new MMODamageMechanic(event.getMechanicName(), event.getConfig()));

        registerPlaceholders();
    }

    private void registerPlaceholders() {

        // MMOItems/MMOCore skill modifier
        MythicMobs.inst().getPlaceholderManager().register("modifier", Placeholder.meta((metadata, arg) -> {
            if (!(metadata instanceof SkillMetadata))
                throw new RuntimeException("Cannot use this placeholder outside of skill");

            Variable var = ((SkillMetadata) metadata).getVariables().get("MMOSkill");
            MythicSkillInfo skillInfo = (MythicSkillInfo) var.get();
            return String.valueOf(skillInfo.getModifier(arg));
        }));

        // MMOItems/MMOCore skill modifier (as int)
        MythicMobs.inst().getPlaceholderManager().register("modifier.int", Placeholder.meta((metadata, arg) -> {
            if (!(metadata instanceof SkillMetadata))
                throw new RuntimeException("Cannot use this placeholder outside of skill");

            Variable var = ((SkillMetadata) metadata).getVariables().get("MMOSkill");
            MythicSkillInfo skillInfo = (MythicSkillInfo) var.get();
            return String.valueOf((int) skillInfo.getModifier(arg));
        }));

        // Stats
        MythicMobs.inst().getPlaceholderManager().register("stat", Placeholder.meta((metadata, arg) -> {
            if (!(metadata instanceof SkillMetadata))
                throw new RuntimeException("Cannot use this placeholder outside of skill");

            Variable var = ((SkillMetadata) metadata).getVariables().get("MMOStatMap");
            StatMap.CachedStatMap statMap = (StatMap.CachedStatMap) var.get();
            return String.valueOf(statMap.getStat(arg.toUpperCase()));
        }));

        // Cooldowns
        MythicMobs.inst().getPlaceholderManager().register("cooldown", Placeholder.meta((metadata, arg) -> {
            CooldownMap cooldownMap = MMOPlayerData.get(metadata.getCaster().getEntity().getUniqueId()).getCooldownMap();
            CooldownInfo info = cooldownMap.getInfo(arg);
            return String.valueOf(info == null ? 0 : info.getRemaining() / 1000d);
        }));
    }

    private String getFaction(Entity entity) {
        ActiveMob mob = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
        return mob != null && mob.hasFaction() ? mob.getFaction() : null;
    }
}
