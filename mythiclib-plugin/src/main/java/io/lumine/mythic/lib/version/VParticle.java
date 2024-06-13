package io.lumine.mythic.lib.version;

import io.lumine.mythic.lib.UtilityMethods;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public enum VParticle {
    EXPLOSION("EXPLOSION", "EXPLOSION_NORMAL"),
    @Deprecated
    LARGE_EXPLOSION("EXPLOSION_EMITTER", "EXPLOSION_LARGE"),
    WITCH("WITCH", "SPELL_WITCH"),
    LARGE_SMOKE("LARGE_SMOKE", "SMOKE_LARGE"),
    SMOKE("SMOKE", "SMOKE_NORMAL"),
    @Deprecated
    REDSTONE("DUST", "REDSTONE"),
    @Deprecated
    FIREWORK("FIREWORK", "FIREWORKS_SPARK"),
    @Deprecated
    INSTANT_EFFECT("INSTANT_EFFECT", "SPELL_INSTANT"),
    @Deprecated
    EFFECT("EFFECT", "SPELL"),
    @Deprecated
    ENTITY_EFFECT("ENTITY_EFFECT", "SPELL_MOB"),
    ENTITY_EFFECT_AMBIENT("ENTITY_EFFECT", "SPELL_MOB_AMBIENT"),
    TOTEM_OF_UNDYING("TOTEM_OF_UNDYING", "TOTEM"),
    HAPPY_VILLAGER("HAPPY_VILLAGER", "VILLAGER_HAPPY"),
    @Deprecated
    SNOWFLAKE("SNOWFLAKE", "SNOWBALL"),
    @Deprecated
    BLOCK("BLOCK", "BLOCK_CRACK"),
    @Deprecated
    BLOCK_DUST("BLOCK", "BLOCK_DUST"),
    ITEM_SNOWBALL("ITEM_SNOWBALL", "SNOW_SHOVEL"),
    ITEM_SLIME("ITEM_SLIME", "SLIME"),
    ENCHANTED_HIT("ENCHANTED_HIT", "CRIT_MAGIC"),
    ITEM("ITEM", "ITEM_CRACK"),

    ;

    private final Particle wrapped;

    VParticle(String... candidates) {
        wrapped = UtilityMethods.resolveEnumField(Particle::valueOf, candidates);
    }

    @NotNull
    public Particle get() {
        return wrapped;
    }
}