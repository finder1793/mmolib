package io.lumine.mythic.lib.skill.custom;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.Mechanic;
import io.lumine.mythic.lib.skill.custom.mechanic.misc.DelayMechanic;
import org.bukkit.Bukkit;

import java.util.Iterator;
import java.util.logging.Level;

/**
 * This class is used to take into account the delay mechanic.
 * Because of this mechanic, you can't cast all the mechanics at
 * the same time and must keep track of what mechanic was cast last.
 * <p>
 * One queue created PER skill casting. If you want to cast the same
 * skill a second time, a second {@link MechanicQueue} must be created
 */
public class MechanicQueue {
    private final Iterator<Mechanic> queue;
    private final SkillMetadata meta;
    private final CustomSkill skill;

    /**
     * Its only use is error messages
     */
    private int counter = 0;

    public MechanicQueue(SkillMetadata meta, CustomSkill skill) {
        this.meta = meta;
        this.queue = skill.getMechanics().iterator();
        this.skill = skill;
    }

    /**
     * Casts the next mechanic on the list. This method is
     * recursive and will call itself until the list has been
     * fully explored.
     * <p>
     * Call this method to start casting the skill.
     */
    public void next() {
        if (!queue.hasNext())
            return;

        counter++;
        Mechanic mechanic = queue.next();

        // Handles the delay mechanic
        if (mechanic instanceof DelayMechanic)
            Bukkit.getScheduler().scheduleSyncDelayedTask(MythicLib.plugin, () -> next(), ((DelayMechanic) mechanic).getDelay(meta));

            // Any other mechanic
        else
            try {
                mechanic.cast(meta);

                // The skill will end here if any error occurs
                next();
            } catch (RuntimeException exception) {
                MythicLib.plugin.getLogger().log(Level.WARNING, "Could not execute mechanic n" + counter + " from skill '" + skill.getId() + "': " + exception.getMessage());
            }
    }
}
