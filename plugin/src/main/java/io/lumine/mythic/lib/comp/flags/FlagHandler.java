package io.lumine.mythic.lib.comp.flags;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlagHandler {
    private final List<FlagPlugin> flagPlugins = new ArrayList<>();

    public void registerPlugin(@NotNull FlagPlugin plugin) {
        flagPlugins.add(Objects.requireNonNull(plugin, "Flag plugin cannot be null"));
    }

    /**
     * True by default when no plugin is registered
     *
     * @return If PvP is toggled on at a specific location.
     */
    public boolean isPvpAllowed(Location loc) {
        for (FlagPlugin plugin : flagPlugins)
            if (!plugin.isPvpAllowed(loc))
                return false;
        return true;
    }

    /**
     * True by default when no plugin is registered
     *
     * @return If some flag is toggled on for some player given their location.
     */
    public boolean isFlagAllowed(Player player, CustomFlag flag) {
        for (FlagPlugin plugin : flagPlugins)
            if (!plugin.isFlagAllowed(player, flag))
                return false;
        return true;
    }

    /**
     * True by default when no plugin is registered
     *
     * @return If some flag is toggled on at a specific location.
     */
    public boolean isFlagAllowed(Location loc, CustomFlag flag) {
        for (FlagPlugin plugin : flagPlugins)
            if (!plugin.isFlagAllowed(loc, flag))
                return false;
        return true;
    }
}
