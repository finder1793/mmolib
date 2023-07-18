package io.lumine.mythic.lib.api.event.skill;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.result.SkillResult;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerCastSkillEvent extends PlayerSkillEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    /**
     * Called after checking that a skill can be cast by a player
     * right before actually applying its effects
     *
     * @param skillMeta Info of the skill being cast
     * @param result    Skill result
     */
    public PlayerCastSkillEvent(SkillMetadata skillMeta, SkillResult result) {
        super(skillMeta, result);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        cancelled = value;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
