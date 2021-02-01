package io.lumine.mythic.lib.sql;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class MMODataSource {
    protected final HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;
    private boolean enabled;

    public void setup(FileConfiguration fileConfig) {
        if (fileConfig.isConfigurationSection("mysql")) {
            ConfigurationSection cfg = fileConfig.getConfigurationSection("mysql");
            if (!cfg.getBoolean("enabled"))
                return;
            enabled = true;
            String sb = "jdbc:mysql://" + cfg.getString("host", "localhost") + ":" + cfg.getString("port", "3306") + "/"
                    + cfg.getString("database", "minecraft");
            config.setJdbcUrl(sb);
            config.setUsername(cfg.getString("user", "mmolover"));
            config.setPassword(cfg.getString("pass", "ILoveAria"));
            if (cfg.isConfigurationSection("properties"))
                for (String s : cfg.getConfigurationSection("properties").getKeys(false)) {
                    config.addDataSourceProperty(s, cfg.getString("properties." + s));
                }
            dataSource = new HikariDataSource(config);
            load();
        }
    }

    protected abstract void load();

    public void getResult(String sql, Consumer<ResultSet> supplier) {
        try {
            Connection connection = getConnection();
            supplier.accept(connection.prepareStatement(sql).executeQuery());
            connection.close();
        } catch (SQLException e) {
            MythicLib.plugin.getLogger().log(Level.SEVERE, "MySQL Operation Failed!");
            e.printStackTrace();
        }
    }

    public CompletableFuture<Void> getResultAsync(String sql, Consumer<ResultSet> supplier) {
        return CompletableFuture.runAsync(() -> getResult(sql, supplier));
    }

    public void executeUpdate(String sql) {
        try {
            Connection connection = getConnection();
            connection.prepareStatement(sql).executeUpdate();
            connection.close();
        } catch (SQLException e) {
            MythicLib.plugin.getLogger().log(Level.SEVERE, "MySQL Operation Failed!");
            e.printStackTrace();
        }
    }

    public CompletableFuture<Void> executeUpdateAsync(String sql) {
        return CompletableFuture.runAsync(() -> executeUpdate(sql));
    }

    public Connection getConnection() throws SQLException {
        if (!enabled)
            throw new IllegalStateException("Can't get SQL Connection while it's disabled!");
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null)
            dataSource.close();
    }

    public boolean isEnabled() {
        return enabled;
    }
}

