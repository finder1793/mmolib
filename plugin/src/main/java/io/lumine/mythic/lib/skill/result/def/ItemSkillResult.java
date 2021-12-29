package io.lumine.mythic.lib.skill.result.def;

import io.lumine.mythic.lib.skill.SkillMetadata;
import org.bukkit.inventory.ItemStack;

/**
 * Skill that requires to throw the item in hand. This takes as input
 * the player's main hand item and also takes the direction where he's looking
 */
public class ItemSkillResult extends VectorSkillResult {
    private final ItemStack item;

    public ItemSkillResult(SkillMetadata skillMeta) {
        super(skillMeta);

        // TODO getItemInMainHand? Breaks the ability when used in the offhand.
        item = skillMeta.getCaster().getPlayer().getInventory().getItemInMainHand();
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public boolean isSuccessful(SkillMetadata skillMeta) {
        return super.isSuccessful(skillMeta) && item != null;
    }
}