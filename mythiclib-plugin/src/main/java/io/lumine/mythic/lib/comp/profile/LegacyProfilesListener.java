package io.lumine.mythic.lib.comp.profile;

import fr.phoenixdevt.profiles.event.ProfileSelectEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class LegacyProfilesListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onProfileChooseSkillTrigger(ProfileSelectEvent event) {
        MMOPlayerData.get(event.getPlayer()).triggerSkills(TriggerType.LOGIN, null);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void updateProfileId(ProfileSelectEvent event) {
        MMOPlayerData.get(event.getPlayer()).setProfileId(event.getProfile().getUniqueId());
    }
}
