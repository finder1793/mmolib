package io.lumine.mythic.lib.command.mythiclib;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.command.api.CommandTreeRoot;
import io.lumine.mythic.lib.command.mythiclib.debug.DebugCommand;
import io.lumine.mythic.lib.command.mythiclib.statmod.TempStatCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class MythicLibCommand extends CommandTreeRoot {
    public MythicLibCommand(@NotNull ConfigurationSection config) {
        super(config, MythicLib.plugin.getCommands().MYTHICLIB);

        addChild(new ReloadCommand(this));
        addChild(new CastCommand(this));
        addChild(new DebugCommand(this));
        addChild(new StatModCommand(this));
        addChild(new TempStatCommand(this));
    }
}