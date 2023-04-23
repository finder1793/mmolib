package io.lumine.mythic.lib.data.sql;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.data.SynchronizedDataHolder;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * This class is used to syncronize player data between
 * servers. This fixes the issue of player data being
 * lost when teleporting to another server.
 */
public abstract class SQLDataSynchronizer<H extends SynchronizedDataHolder> {
    private final SQLDataSource dataSource;
    private final H data;
    private final String tableName, uuidFieldName;
    private final long start = System.currentTimeMillis();

    private int tries;

    public SQLDataSynchronizer(String tableName, String uuidFieldName, SQLDataSource dataSource, H data) {
        this.tableName = tableName;
        this.uuidFieldName = uuidFieldName;
        this.data = data;
        this.dataSource = dataSource;
    }

    public H getData() {
        return data;
    }

    private static final int PERIOD = 1000;

    /**
     * Tries to fetch data once. If the maximum amount of fetches
     * hasn't been reached yet, it will try again later if no up-to-date
     * data has been retrieved.
     * <p>
     * This method freezes the thread and shall be called async.
     */
    public void synchronize() {

        // Cancel if player is offline
        if (!data.getMMOPlayerData().isOnline()) {
            UtilityMethods.debug(dataSource.getPlugin(), "SQL", "Stopped data retrieval for '" + data.getProfileId() + "' as they went offline");
            return;
        }

        tries++;

        // Fields that must be closed afterwards
        @Nullable Connection connection = null;
        @Nullable PreparedStatement prepared = null;
        @Nullable ResultSet result = null;
        boolean retry = false;

        try {
            connection = dataSource.getConnection();
            prepared = connection.prepareStatement("SELECT * FROM `" + tableName + "` WHERE `" + uuidFieldName + "` = ?;");
            prepared.setString(1, data.getProfileId().toString());

            UtilityMethods.debug(dataSource.getPlugin(), "SQL", "Trying to load data of " + data.getProfileId());
            result = prepared.executeQuery();

            // Load data if found
            if (result.next()) {
                if (tries > MythicLib.plugin.getMMOConfig().maxSyncTries || result.getInt("is_saved") == 1) {
                    confirmReception(connection);
                    loadData(result);
                    if (tries > MythicLib.plugin.getMMOConfig().maxSyncTries)
                        UtilityMethods.debug(dataSource.getPlugin(), "SQL", "Maximum number of tries reached.");
                    UtilityMethods.debug(dataSource.getPlugin(), "SQL", "Found and loaded data of '" + data.getProfileId() + "'");
                    UtilityMethods.debug(dataSource.getPlugin(), "SQL", "Time taken: " + (System.currentTimeMillis() - start) + "ms");
                } else {
                    UtilityMethods.debug(dataSource.getPlugin(), "SQL", "Did not load data of '" + data.getProfileId() + "' as 'is_saved' is set to 0, trying again in " + PERIOD + "ms");
                    retry = true;
                    Thread.sleep(PERIOD);
                }
            } else {

                // Empty player data
                confirmReception(connection);
                loadEmptyData();
                UtilityMethods.debug(dataSource.getPlugin(), "SQL", "Found empty data for '" + data.getProfileId() + "', loading default...");
            }

        } catch (Exception throwable) {
            dataSource.getPlugin().getLogger().log(Level.WARNING, "Could not load player data of '" + data.getProfileId() + "':");
            throwable.printStackTrace();
        } finally {

            // Close resources
            try {
                if (result != null) result.close();
                if (prepared != null) prepared.close();
                if (connection != null) connection.close();
            } catch (SQLException exception) {
                dataSource.getPlugin().getLogger().log(Level.WARNING, "Could not load player data of '" + data.getProfileId() + "':");
                exception.printStackTrace();
            }
        }

        // Synchronize after closing resources
        if (retry) synchronize();
    }

    /**
     * This confirms the loading of player and switches "is_saved" back to 0
     *
     * @param connection Current SQL connection
     * @throws SQLException Any exception. When thrown, the data will not be loaded.
     */
    private void confirmReception(Connection connection) throws SQLException {

        // Confirm reception of inventory
        final PreparedStatement prepared1 = connection.prepareStatement("INSERT INTO " + tableName + "(`uuid`, `is_saved`) VALUES(?, 0) ON DUPLICATE KEY UPDATE `is_saved` = 0;");
        prepared1.setString(1, data.getProfileId().toString());
        try {
            prepared1.executeUpdate();
        } catch (Exception exception) {
            dataSource.getPlugin().getLogger().log(Level.WARNING, "Could not confirm data sync of " + data.getProfileId());
            exception.printStackTrace();
        } finally {
            prepared1.close();
        }
    }

    /**
     * Called when the right result set has finally been found.
     *
     * @param result Row found in the database
     */
    public abstract void loadData(ResultSet result) throws SQLException, IOException, ClassNotFoundException;

    /**
     * Called when no data was found.
     */
    public abstract void loadEmptyData();
}
