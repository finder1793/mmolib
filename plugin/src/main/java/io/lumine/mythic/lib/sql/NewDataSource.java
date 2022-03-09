package io.lumine.mythic.lib.sql;

import io.lumine.utils.storage.sql.hikari.HikariConfig;
import io.lumine.utils.storage.sql.hikari.HikariDataSource;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class NewDataSource extends HikariDataSource {
    private final boolean debugMode;

    public NewDataSource(FileConfiguration config) {
        super(loadConfig(config));

        // Extra options
        this.debugMode = config.getBoolean("debug");

        whenLoaded();
    }

    private static HikariConfig loadConfig(FileConfiguration cfg) {
        Validate.isTrue(cfg.getBoolean("enabled", false), "MySQL is not enabled");

        HikariConfig config = new HikariConfig();
        config.setPoolName("MMO-hikari");

        String databaseUrl = "jdbc:mysql://" + cfg.getString("host", "localhost") + ":" + cfg.getString("port", "3306") + "/"
                + cfg.getString("database", "minecraft");

        config.setJdbcUrl(databaseUrl);
        config.setUsername(cfg.getString("user", "mmolover"));
        config.setPassword(cfg.getString("pass", "ILoveAria"));
        config.setMaximumPoolSize(cfg.getInt("maxPoolSize", 10));
        config.setMaxLifetime(cfg.getLong("maxLifeTime", 300000));
        config.setConnectionTimeout(cfg.getLong("connectionTimeOut", 10000));
        config.setLeakDetectionThreshold(cfg.getLong("leakDetectionThreshold", 150000));

        if (cfg.isConfigurationSection("properties"))
            for (String key : cfg.getConfigurationSection("properties").getKeys(false))
                config.addDataSourceProperty(key, cfg.getString("properties." + key));
        return config;
    }

    protected abstract void whenLoaded();

    public void getResult(String sql, Consumer<ResultSet> supplier) {
        execute(connection -> {
            try {
                supplier.accept(connection.prepareStatement(sql).executeQuery());
            } catch (SQLException exception) {
                exception.printStackTrace();
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
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> executeUpdateAsync(String sql) {
        return CompletableFuture.runAsync(() -> executeUpdate(sql));
    }

    /**
     * Retrieves a connection from pool, prepares it for use
     * and even closes it when it's finished using it.
     *
     * @param execute Consumer.
     */
    public void execute(Consumer<Connection> execute) {
        try {
            execute.accept(getConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean isDebugMode() {
        return debugMode;
    }
}

