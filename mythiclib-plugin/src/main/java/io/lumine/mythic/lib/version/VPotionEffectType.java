package io.lumine.mythic.lib.version;

import io.lumine.mythic.lib.UtilityMethods;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public enum VPotionEffectType {
    NAUSEA("NAUSEA", "CONFUSION"),
    @Deprecated // TODO use UtilityMethods#forcePotionEffect
    SLOWNESS("SLOWNESS", "SLOW"),
    JUMP_BOOST("JUMP_BOOST", "JUMP"),
    MINING_FATIGUE("MINING_FATIGUE", "SLOW_DIGGING"),

    ;

    private final PotionEffectType wrapped;

    VPotionEffectType(String... candidates) {
        wrapped = UtilityMethods.resolveEnumField(PotionEffectType::getByName, candidates);
    }

    @NotNull
    public PotionEffectType get() {
        return wrapped;
    }
}