package io.lumine.mythic.lib;

import io.lumine.mythic.lib.api.crafting.recipes.MythicCraftingManager;
import io.lumine.mythic.lib.api.placeholders.MythicPlaceholders;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.commands.BaseCommand;
import io.lumine.mythic.lib.comp.CitizensEntityHandler;
import io.lumine.mythic.lib.comp.MythicMobsDamageHandler;
import io.lumine.mythic.lib.comp.PlaceholderAPIHook;
import io.lumine.mythic.lib.comp.hexcolor.ColorParser;
import io.lumine.mythic.lib.comp.hexcolor.HexColorParser;
import io.lumine.mythic.lib.comp.hexcolor.SimpleColorParser;
import io.lumine.mythic.lib.gui.PluginInventory;
import io.lumine.mythic.lib.listener.*;
import io.lumine.mythic.lib.listener.event.PlayerAttackEventListener;
import io.lumine.mythic.lib.manager.*;
import io.lumine.mythic.lib.metrics.bStats;
import io.lumine.mythic.lib.mmolibcommands.ExploreAttributesCommand;
import io.lumine.mythic.lib.mmolibcommands.MMODebugCommand;
import io.lumine.mythic.lib.mmolibcommands.MMOLibCommand;
import io.lumine.mythic.lib.mmolibcommands.MMOTempStatCommand;
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

import java.util.logging.Level;

public class MythicLib extends LuminePlugin {
	public static MythicLib plugin;

	//@Getter private ProfileManager profileManager;

	private final DamageManager damageManager = new DamageManager();
	private final EntityManager entityManager = new EntityManager();
	private final StatManager statManager = new StatManager();
	private final JsonManager jsonManager = new JsonManager();
	private final ConfigManager configManager = new ConfigManager();

	private ServerVersion version;
	private AttackEffects attackEffects;
	private MitigationMechanics mitigationMechanics;
	private ColorParser colorParser;

	@Getter
	private ScoreboardProvider scoreboardProvider;
	@Getter
	private HologramFactory hologramProvider;

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

	}

	@Override
	public void enable() {

		//this.bind(this.configuration = new Configuration(this));
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

		this.scoreboardProvider = new PacketScoreboardProvider(this);
		this.provideService(ScoreboardProvider.class, this.scoreboardProvider);

		this.hologramProvider = new BukkitHologramFactory();
		this.provideService(HologramFactory.class, this.hologramProvider);

		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

		Bukkit.getPluginManager().registerEvents(damageManager, this);
		Bukkit.getPluginManager().registerEvents(new DamageReduction(), this);
		Bukkit.getPluginManager().registerEvents(attackEffects = new AttackEffects(), this);
		Bukkit.getPluginManager().registerEvents(mitigationMechanics = new MitigationMechanics(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerAttackEventListener(), this);
		Bukkit.getPluginManager().registerEvents(new ArmorEquipEventListener(), this);
		Bukkit.getPluginManager().registerEvents(new MythicCraftingManager(), this);

		if (getConfig().getBoolean("health-scale.enabled"))
			Bukkit.getPluginManager().registerEvents(new HealthScale(getConfig().getDouble("health-scale.scale"), getConfig().getInt("health-scale.delay", 0)), this);

		if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
			damageManager.registerHandler(new MythicMobsDamageHandler());
			this.hasMythicMobs = true;
		}

		if (Bukkit.getPluginManager().getPlugin("Citizens") != null)
			entityManager.registerHandler(new CitizensEntityHandler());

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			MythicPlaceholders.registerPlaceholder(new PlaceholderAPIHook());
		}

//		if (Bukkit.getPluginManager().getPlugin("ShopKeepers") != null)
//			entityManager.registerHandler(new ShopKeepersEntityHandler());

		if (version.isStrictlyHigher(1, 12))
			getCommand("exploreattributes").setExecutor(new ExploreAttributesCommand());
		getCommand("mythiclib").setExecutor(new MMOLibCommand());
		getCommand("mmodebug").setExecutor(new MMODebugCommand());
		getCommand("mmotempstat").setExecutor(new MMOTempStatCommand());

		// Load player data of online players
		Bukkit.getOnlinePlayers().forEach(player -> MMOPlayerData.setup(player.getUniqueId()).updatePlayer(player));

		configManager.reload();

	}

	public void reload() {
		reloadConfig();
		configManager.reload();
		attackEffects.reload();
		mitigationMechanics.reload();
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

	public StatManager getStats() {
		return statManager;
	}

	public ConfigManager getMMOConfig() {
		return configManager;
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
