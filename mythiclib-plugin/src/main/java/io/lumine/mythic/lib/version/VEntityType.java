package io.lumine.mythic.lib.version;

import io.lumine.mythic.lib.UtilityMethods;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum VEntityType {
    TNT("TNT", "PRIMED_TNT"),
    SNOW_GOLEM("SNOW_GOLEM", "SNOWMAN"),
    ;

    private final EntityType wrapped;

    VEntityType(String... candidates) {
        wrapped = UtilityMethods.resolveEnumField(EntityType::valueOf, candidates);
    }

    @NotNull
    public EntityType get() {
        return wrapped;
    }
}