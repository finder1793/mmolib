package io.lumine.mythic.lib.comp.profile;

import fr.phoenixdevt.profiles.ProfileDataModule;
import fr.phoenixdevt.profiles.ProfileProvider;
import fr.phoenixdevt.profiles.event.ProfileSelectEvent;
import fr.phoenixdevt.profiles.event.ProfileUnloadEvent;
import fr.phoenixdevt.profiles.placeholder.PlaceholderProcessor;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.event.SynchronizedDataLoadEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.data.SynchronizedDataHolder;
import io.lumine.mythic.lib.data.SynchronizedDataManager;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class LegacyProfiles implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onProfileChooseSkillTrigger(ProfileSelectEvent event) {
        MMOPlayerData.get(event.getPlayer()).triggerSkills(TriggerType.LOGIN, null);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void updateProfileId(ProfileSelectEvent event) {
        MMOPlayerData.get(event.getPlayer()).setProfileId(event.getProfile().getUniqueId());
    }

    /**
     * This is used to hook plugins which support legacy profiles:
     * - MMOCore
     * - MMOItems
     * - MMOInventory
     * <p>
     * This class tells these plugins to load data when a profile is being
     * selected by a player. By default, player data loads on login so this
     * has to be changed to support profiles.
     */
    public static <H extends SynchronizedDataHolder> void hook(@NotNull SynchronizedDataManager<H, ?> manager,
                                                         @NotNull Listener fictiveListener,
                                                         @NotNull EventPriority joinEventPriority,
                                                         @NotNull EventPriority quitEventPriority) {

        // Register data holder
        final ProfileProvider profilePlugin = Bukkit.getServicesManager().getRegistration(ProfileProvider.class).getProvider();
        final ProfileDataModule module = (ProfileDataModule) manager.newProfileDataModule();
        profilePlugin.registerModule(module);
        if (module instanceof PlaceholderProcessor) profilePlugin.registerPlaceholders((PlaceholderProcessor) module);
        manager.getOwningPlugin().getLogger().log(Level.INFO, "Hooked onto Profiles");

        // Load data on profile select
        Bukkit.getPluginManager().registerEvent(ProfileSelectEvent.class, fictiveListener, joinEventPriority, (listener, evt) -> {
            final ProfileSelectEvent event = (ProfileSelectEvent) evt;
            final @NotNull H data = manager.get(event.getPlayer());
            if (data.isSynchronized()) event.validate(module); // More resilience
            else
                manager.loadData(data).thenAccept(UtilityMethods.sync(manager.getOwningPlugin(), v -> {
                    event.validate(module);
                    data.markAsSynchronized();
                    Bukkit.getPluginManager().callEvent(new SynchronizedDataLoadEvent(manager, data, event));
                }));
        }, manager.getOwningPlugin());

        // TODO Remove data on profile removal

        // Save data on profile unload
        Bukkit.getPluginManager().registerEvent(ProfileUnloadEvent.class, fictiveListener, quitEventPriority, (listener, evt) -> {
            final ProfileUnloadEvent event = (ProfileUnloadEvent) evt;
            manager.unregister(event.getPlayer()).thenAccept(UtilityMethods.sync(manager.getOwningPlugin(), v -> event.validate(module)));
        }, manager.getOwningPlugin());
    }
}
