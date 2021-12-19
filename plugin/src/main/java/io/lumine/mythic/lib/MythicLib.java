package io.lumine.mythic.lib;

import io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager;
import io.lumine.mythic.lib.api.crafting.recipes.vmp.MegaWorkbenchMapping;
import io.lumine.mythic.lib.api.crafting.recipes.vmp.SuperWorkbenchMapping;
import io.lumine.mythic.lib.api.placeholders.MythicPlaceholders;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.commands.BaseCommand;
import io.lumine.mythic.lib.commands.HealthScaleCommand;
import io.lumine.mythic.lib.comp.placeholder.DefaultPlaceholderParser;
import io.lumine.mythic.lib.comp.placeholder.PlaceholderAPIHook;
import io.lumine.mythic.lib.comp.flags.DefaultFlagHandler;
import io.lumine.mythic.lib.comp.flags.FlagPlugin;
import io.lumine.mythic.lib.comp.flags.ResidenceFlags;
import io.lumine.mythic.lib.comp.flags.WorldGuardFlags;
import io.lumine.mythic.lib.comp.hexcolor.ColorParser;
import io.lumine.mythic.lib.comp.hexcolor.HexColorParser;
import io.lumine.mythic.lib.comp.hexcolor.SimpleColorParser;
import io.lumine.mythic.lib.comp.hologram.CustomHologramFactoryList;
import io.lumine.mythic.lib.comp.mythicmobs.MythicMobsAttackHandler;
import io.lumine.mythic.lib.comp.mythicmobs.MythicMobsHook;
import io.lumine.mythic.lib.comp.placeholder.PlaceholderAPIParser;
import io.lumine.mythic.lib.comp.placeholder.PlaceholderParser;
import io.lumine.mythic.lib.comp.target.CitizensTargetRestriction;
import io.lumine.mythic.lib.comp.target.FactionsRestriction;
import io.lumine.mythic.lib.gui.PluginInventory;
import io.lumine.mythic.lib.listener.*;
import io.lumine.mythic.lib.listener.event.PlayerAttackEventListener;
import io.lumine.mythic.lib.listener.option.DamageIndicators;
import io.lumine.mythic.lib.listener.option.FixMovementSpeed;
import io.lumine.mythic.lib.listener.option.RegenIndicators;
import io.lumine.mythic.lib.manager.*;
import io.lumine.mythic.lib.metrics.bStats;
import io.lumine.mythic.lib.commands.mmolib.ExploreAttributesCommand;
import io.lumine.mythic.lib.commands.mmolib.MMODebugCommand;
import io.lumine.mythic.lib.commands.mmolib.MMOLibCommand;
import io.lumine.mythic.lib.commands.mmolib.MMOTempStatCommand;
import io.lumine.mythic.lib.version.ServerVersion;
import io.lumine.mythic.lib.version.SpigotPlugin;
import io.lumine.utils.events.extra.ArmorEquipEventListener;
import io.lumine.utils.holograms.BukkitHologramFactory;
import io.lumine.utils.holograms.HologramFactory;
import io.lumine.utils.plugin.LuminePlugin;
import io.lumine.utils.scoreboard.PacketScoreboardProvider;
import io.lumine.utils.scoreboard.ScoreboardProvider;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

import java.util.logging.Level;

public class MythicLib extends LuminePlugin {
    public static MythicLib plugin;

    //@Getter private ProfileManager profileManager;

    private final DamageManager damageManager = new DamageManager();
    private final EntityManager entityManager = new EntityManager();
    private final StatManager statManager = new StatManager();
    private final JsonManager jsonManager = new JsonManager();
    private final ConfigManager configManager = new ConfigManager();
    private final ElementManager elementManager = new ElementManager();
    private final SkillManager skillManager = new SkillManager();

    private ServerVersion version;
    private AttackEffects attackEffects;
    private MitigationMechanics mitigationMechanics;
    private ColorParser colorParser;
    private FlagPlugin flagPlugin = new DefaultFlagHandler();
    @Getter
    private ScoreboardProvider scoreboardProvider;
    private PlaceholderParser placeholderParser;

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

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            flagPlugin = new WorldGuardFlags();
            getLogger().log(Level.INFO, "Hooked onto WorldGuard");
        }

        colorParser = version.isBelowOrEqual(1, 15) ? new SimpleColorParser() : new HexColorParser();
    }

    @Override
    public void enable() {
        registerCommand("mythiclib", new BaseCommand(this));

        new bStats(this);

        new SpigotPlugin(90306, this).checkForUpdate();
        saveDefaultConfig();

        final int configVersion = getConfig().contains("config-version", true) ? getConfig().getInt("config-version") : -1;
        final int defConfigVersion = getConfig().getDefaults().getInt("config-version");
        if (configVersion != defConfigVersion) {
            getLogger().warning("You may be using an outdated config.yml!");
            getLogger().warning("(Your config version: '" + configVersion + "' | Expected config version: '" + defConfigVersion + "')");
        }

        this.scoreboardProvider = new PacketScoreboardProvider(this);
        this.provideService(ScoreboardProvider.class, this.scoreboardProvider);

        // Hologram provider
        this.provideService(HologramFactory.class, new BukkitHologramFactory(), ServicePriority.Low);

        // Register listeners
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(damageManager, this);
        Bukkit.getPluginManager().registerEvents(new DamageReduction(), this);
        Bukkit.getPluginManager().registerEvents(attackEffects = new AttackEffects(), this);
        Bukkit.getPluginManager().registerEvents(mitigationMechanics = new MitigationMechanics(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerAttackEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorEquipEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new MythicCraftingManager(), this);
        Bukkit.getPluginManager().registerEvents(new SkillTriggers(), this);

        if (getConfig().getBoolean("health-scale.enabled"))
            Bukkit.getPluginManager().registerEvents(new HealthScale(getConfig().getDouble("health-scale.scale"), getConfig().getInt("health-scale.delay", 0)), this);

        if (getConfig().getBoolean("fix-movement-speed"))
            Bukkit.getPluginManager().registerEvents(new FixMovementSpeed(), this);

        // Custom hologram providers
        for (CustomHologramFactoryList custom : CustomHologramFactoryList.values())
            if (custom.isInstalled(getServer().getPluginManager()))
                try {
                    provideService(HologramFactory.class, custom.generateFactory(), custom.getServicePriority());
                    getLogger().log(Level.INFO, "Hooked onto " + custom.getPluginName());
                } catch (Exception exception) {
                    getLogger().log(Level.WARNING, "Could not hook onto " + custom.getPluginName() + ": " + exception.getMessage());
                }

        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            damageManager.registerHandler(new MythicMobsAttackHandler());
            Bukkit.getPluginManager().registerEvents(new MythicMobsHook(), this);
            getLogger().log(Level.INFO, "Hooked onto MythicMobs");
        }

        if (Bukkit.getPluginManager().getPlugin("Residence") != null) {
            flagPlugin = new ResidenceFlags();
            getLogger().log(Level.INFO, "Hooked onto Residence");
        }

        if (Bukkit.getPluginManager().getPlugin("Factions") != null) {
            entityManager.registerRestriction(new FactionsRestriction());
            getLogger().log(Level.INFO, "Hooked onto Factions");
        }

        if (Bukkit.getPluginManager().getPlugin("Citizens") != null) {
            entityManager.registerRestriction(new CitizensTargetRestriction());
            getLogger().log(Level.INFO, "Hooked onto Citizens");
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            MythicPlaceholders.registerPlaceholder(new PlaceholderAPIHook());
            placeholderParser = new PlaceholderAPIParser();
            getLogger().log(Level.INFO, "Hooked onto PlaceholderAPI");
        } else
            placeholderParser = new DefaultPlaceholderParser();

        // Regen and damage indicators
        if (getConfig().getBoolean("game-indicators.damage.enabled"))
            Bukkit.getPluginManager().registerEvents(new DamageIndicators(getConfig().getConfigurationSection("game-indicators.damage")), this);
        if (getConfig().getBoolean("game-indicators.regen.enabled"))
            Bukkit.getPluginManager().registerEvents(new RegenIndicators(getConfig().getConfigurationSection("game-indicators.regen")), this);

//		if (Bukkit.getPluginManager().getPlugin("ShopKeepers") != null)
//			entityManager.registerHandler(new ShopKeepersEntityHandler());

        if (version.isStrictlyHigher(1, 12))
            getCommand("exploreattributes").setExecutor(new ExploreAttributesCommand());
        getCommand("mythiclib").setExecutor(new MMOLibCommand());
        getCommand("mmodebug").setExecutor(new MMODebugCommand());
        getCommand("mmotempstat").setExecutor(new MMOTempStatCommand());
        getCommand("healthscale").setExecutor(new HealthScaleCommand());

        // Super workbench
        getCommand("superworkbench").setExecutor(SuperWorkbenchMapping.SWB);
        Bukkit.getPluginManager().registerEvents(SuperWorkbenchMapping.SWB, this);
        getCommand("megaworkbench").setExecutor(MegaWorkbenchMapping.MWB);
        Bukkit.getPluginManager().registerEvents(MegaWorkbenchMapping.MWB, this);

        // Load local skills
        skillManager.loadLocalSkills();

        // Load player data of online players
        Bukkit.getOnlinePlayers().forEach(player -> MMOPlayerData.setup(player));

        configManager.reload();
    }

    public void reload() {
        reloadConfig();
        configManager.reload();
        attackEffects.reload();
        mitigationMechanics.reload();
        skillManager.loadLocalSkills();
    }

    @Override
    public void disable() {
        //this.configuration.unload();
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getOpenInventory() != null && player.getOpenInventory().getTopInventory().getHolder() != null && player.getOpenInventory().getTopInventory().getHolder() instanceof PluginInventory)
                player.closeInventory();
    }

    public static MythicLib inst() {
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

    /**
     * @deprecated Not implemented yet
     */
    @Deprecated
    public SkillManager getSkills() { return skillManager; }

    /**
     * @deprecated Not implemented yet
     */
    @Deprecated
    public ElementManager getElements() {
        return elementManager;
    }

    public StatManager getStats() {
        return statManager;
    }

    public ConfigManager getMMOConfig() {
        return configManager;
    }

    public FlagPlugin getFlags() {
        return flagPlugin;
    }

    public PlaceholderParser getPlaceholderParser() {
        return placeholderParser;
    }

    public AttackEffects getAttackEffects() {
        return attackEffects;
    }

    public void handleFlags(FlagPlugin flagPlugin) {
        this.flagPlugin = flagPlugin;
    }

    /**
     * @param format The string to format
     * @return String with parsed (hex) color codes
     */
    public String parseColors(String format) {
        return colorParser.parseColorCodes(format);
    }
}
