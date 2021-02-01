package io.lumine.mythic.lib;

import io.lumine.mythic.lib.commands.BaseCommand;
import io.lumine.mythic.lib.config.Configuration;
import io.lumine.mythic.lib.metrics.bStats;
import io.lumine.mythic.lib.version.ServerVersion;
import io.lumine.utils.events.extra.ArmorEquipEventListener;
import io.lumine.utils.logging.Log;
import io.lumine.utils.plugin.LuminePlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class MythicLib extends LuminePlugin {

    private static MythicLib plugin;
 
    @Getter private Configuration configuration; 
    //@Getter private ProfileManager profileManager;

    private final DamageManager damageManager = new DamageManager();
    private final EntityManager entityManager = new EntityManager();
    private final StatManager statManager = new StatManager();
    private final JsonManager jsonManager = new JsonManager();
    private final ConfigManager configManager = new ConfigManager();

    private net.mmogroup.mmolib.version.ServerVersion version;
    private AttackEffects attackEffects;
    private MitigationMecanics mitigationMecanics;
    private ColorParser colorParser;
    private ComponentBuilder componentBuilder;

    private boolean hasMythicMobs = false;

    @Override
    public void load() {
        plugin = this;

        try {
            version = new ServerVersion(Bukkit.getServer().getClass());
            getLogger().log(Level.INFO, "Detected Bukkit Version: " + version.toString());
        } catch (Exception exception) {
            getLogger().log(Level.INFO, net.md_5.bungee.api.ChatColor.RED + "Your server version is not compatible.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        colorParser = version.isBelowOrEqual(1, 15) ? new SimpleColorParser() : new HexColorParser();
        componentBuilder = new ComponentBuilder();
    }
     
    @Override
    public void enable() {
        Log.info(ChatColor.GOLD + "-------------------------------------------------");
        Log.info(ChatColor.AQUA + "+ Infecting Server with MythicLib for Spigot/Paper");
        Log.info(ChatColor.GOLD + "-------------------------------------------------");

        this.bind(this.configuration = new Configuration(this));
        //this.profileManager = new ProfileManager(this);

        //this.bind(this.profileManager);

        registerCommand("mythiclib", new BaseCommand(this));

        new bStats(this);

        new SpigotPlugin(73855, this).checkForUpdate();
        saveDefaultConfig();

        final int configVersion = getConfig().contains("config-version", true) ? getConfig().getInt("config-version") : -1;
        final int defConfigVersion = getConfig().getDefaults().getInt("config-version");
        if (configVersion != defConfigVersion) {
            getLogger().warning("You may be using an outdated config.yml!");
            getLogger().warning("(Your config version: '" + configVersion + "' | Expected config version: '" + defConfigVersion + "')");
        }

        Bukkit.getPluginManager().registerEvents(damageManager, this);
        Bukkit.getPluginManager().registerEvents(new DamageReduction(), this);
        Bukkit.getPluginManager().registerEvents(attackEffects = new AttackEffects(), this);
        Bukkit.getPluginManager().registerEvents(mitigationMecanics = new MitigationMecanics(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerAttackEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipEventListener(), this);

        if (getConfig().getBoolean("health-scale.enabled"))
            Bukkit.getPluginManager().registerEvents(new HealthScale(getConfig().getDouble("health-scale.scale")), this);

        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            damageManager.registerHandler(new MythicMobsDamageHandler());
            this.hasMythicMobs = true;
        }

        if (Bukkit.getPluginManager().getPlugin("Citizens") != null)
            entityManager.registerHandler(new CitizensEntityHandler());

        if (Bukkit.getPluginManager().getPlugin("ShopKeepers") != null)
            entityManager.registerHandler(new ShopKeepersEntityHandler());

        if (Bukkit.getPluginManager().getPlugin("MyPet") != null)
            entityManager.registerHandler(new MyPetEntityHandler());

        if (version.isStrictlyHigher(1, 12))
            getCommand("exploreattributes").setExecutor(new ExploreAttributesCommand());
        getCommand("mmolib").setExecutor(new MMOLibCommand());
        getCommand("mmodebug").setExecutor(new MMODebugCommand());
        getCommand("mmotempstat").setExecutor(new MMOTempStatCommand());

        if (getConfig().getBoolean("fix-player-attributes")) AttributeStatHandler.updateAttributes = true;

        Bukkit.getOnlinePlayers().forEach(MMOPlayerData::setup);

        configManager.reload();

    }

    public void reload() {
        reloadConfig();
        configManager.reload();
        attackEffects.reload();
        mitigationMecanics.reload();
    }

    @Override
    public void disable() {
        //this.configuration.unload();
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getOpenInventory() != null && player.getOpenInventory().getTopInventory().getHolder() != null && player.getOpenInventory().getTopInventory().getHolder() instanceof PluginInventory)
                player.closeInventory();
    }
    
    public static MythicLib inst()    {
        return plugin;
    }
    public ServerVersion getVersion() {
        return version;
    }

    public JsonManager getJson() {
        return jsonManager;
    }

    public DamageManager getDamage() {
        return damageManager;
    }

    public EntityManager getEntities() {
        return entityManager;
    }

    public StatManager getStats() {
        return statManager;
    }

    public ConfigManager getMMOConfig() {
        return configManager;
    }

    public ComponentBuilder getComponentBuilder() {
        return componentBuilder;
    }

    /**
     * @param format The string to format
     * @return String with parsed (hex) color codes
     */
    public String parseColors(String format) {
        return colorParser.parseColorCodes(format);
    }

    /*
     * saving if mythic mobs is enabled because it gets called every player
     * attack event
     */
    public boolean hasMythicMobs() {
        return hasMythicMobs;
    }
}
