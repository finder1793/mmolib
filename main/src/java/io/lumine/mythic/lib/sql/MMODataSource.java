package io.lumine.mythic.lib.sql;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.storage.sql.hikari.HikariConfig;
import io.lumine.utils.storage.sql.hikari.HikariDataSource;

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

            config.setPoolName("MMO-hikari");

            String sb = "jdbc:mysql://" + cfg.getString("host", "localhost") + ":" + cfg.getString("port", "3306") + "/"
                    + cfg.getString("database", "minecraft");

            config.setJdbcUrl(sb);
            config.setUsername(cfg.getString("user", "mmolover"));
            config.setPassword(cfg.getString("pass", "ILoveAria"));
            config.setMaximumPoolSize(cfg.getInt("maxPoolSize", 10));
            config.setMaxLifetime(cfg.getLong("maxLifeTime", 300000));
            config.setConnectionTimeout(cfg.getLong("connectionTimeOut", 10000));
            config.setLeakDetectionThreshold(cfg.getLong("leakDetectionThreshold", 150000));

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
        execute(connection -> {
            try {
                supplier.accept(connection.prepareStatement(sql).executeQuery());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> getResultAsync(String sql, Consumer<ResultSet> supplier) {
        return CompletableFuture.runAsync(() -> getResult(sql, supplier));
    }

    public void executeUpdate(String sql) {
        execute(connection -> {
            try {
                connection.prepareStatement(sql).executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> executeUpdateAsync(String sql) {
        return CompletableFuture.runAsync(() -> executeUpdate(sql));
    }

    /**
     * Retrieve a connection from pool and prepare it for use and even closes it when it's finished using it.
     * @param execute Consumer.
     */
    public void execute(Consumer<Connection> execute) {
        if (!enabled)
            throw new IllegalStateException("Can't get SQL Connection while it's disabled!");

        try (Connection connection = dataSource.getConnection()) {
            execute.accept(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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

