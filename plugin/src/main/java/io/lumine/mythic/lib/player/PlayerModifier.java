package io.lumine.mythic.lib.player;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.modifier.ModifierSource;

/**
 * A player modifier can be anything that is temporarily assigned
 * to a player including the following
 * - stat modifier
 * - potion effect
 * - any passive skill
 * <p>
 * Since MythicLib 1.2 it is a generalization of stat modifiers
 *
 * @author indyuce
 */
public abstract class PlayerModifier {
    private final ModifierSource source;
    private final EquipmentSlot slot;

    protected PlayerModifier(EquipmentSlot slot, ModifierSource source) {
        this.slot = slot;
        this.source = source;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    public ModifierSource getSource() {
        return source;
    }
}
