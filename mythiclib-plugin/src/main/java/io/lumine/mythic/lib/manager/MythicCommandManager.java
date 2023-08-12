package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.command.api.MMOCommandManager;
import io.lumine.mythic.lib.command.api.ToggleableCommand;
import io.lumine.mythic.lib.command.mythiclib.MythicLibCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class MythicCommandManager extends MMOCommandManager {

    public ToggleableCommand MYTHIC_LIB = new ToggleableCommand("ml", "mythiclib.admin",
            "MythicLib Main command", MythicLibCommand::new);


    @Override
    public JavaPlugin getPlugin() {
        return MythicLib.plugin;
    }

    @Override
    public List<ToggleableCommand> getAll() {
        return Arrays.asList(MYTHIC_LIB);
    }
}
