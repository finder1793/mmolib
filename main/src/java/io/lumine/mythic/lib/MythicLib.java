package io.lumine.mythic.lib;

import org.bukkit.ChatColor;

import io.lumine.mythic.lib.commands.BaseCommand;
import io.lumine.mythic.lib.config.Configuration;
import io.lumine.mythic.lib.metrics.bStats;
import io.lumine.utils.Schedulers;
import io.lumine.utils.logging.Log;
import io.lumine.utils.plugin.LuminePlugin;
import lombok.Getter;

public class MythicLib extends LuminePlugin {

    private static MythicLib plugin;
 
    @Getter private Configuration configuration; 
    //@Getter private ProfileManager profileManager;

    @Override
    public void load() {
        plugin = this;
    }
     
    @Override
    public void enable() {
        Log.info(ChatColor.GOLD + "-------------------------------------------------");
        Log.info(ChatColor.AQUA + "+ Infecting Server with MythicLib for Bukkit");
        Log.info(ChatColor.GOLD + "-------------------------------------------------");

        this.bind(this.configuration = new Configuration(this));
        //this.profileManager = new ProfileManager(this);

        //this.bind(this.profileManager);

        registerCommand("mythiclib", new BaseCommand(this));

        new bStats(this);
    }
    
    @Override
    public void disable() {
        //this.configuration.unload();
    }
    
    public static MythicLib inst()    {
        return plugin;
    }
}
