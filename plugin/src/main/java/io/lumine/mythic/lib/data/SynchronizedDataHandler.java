package io.lumine.mythic.lib.data;

import io.lumine.mythic.lib.player.modifier.Closeable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A data handles "handles" data, it decides what happens when
 * data is either saved OR loaded from either SQL or YAML. Remember
 * that {@link SynchronizedDataManager} does not know if we are using
 * SQL or YAML.
 */
public interface SynchronizedDataHandler<H extends SynchronizedDataHolder, O extends OfflineDataHolder> extends Closeable {

    public void setup();

    /**
     * Called when player data must be saved in configs or database.
     * This method is always called async, no need to wrap requests
     * inside of async runnables.
     *
     * @param playerData Player data to save
     * @param autosave   When logging out, is_saved is switched back to 1. This behaviour
     *                   must be skipped when autosaving otherwise this will mess with
     *                   the database.
     */
    public void saveData(H playerData, boolean autosave);

    /**
     * This method is always called async, therefore it may send
     * HTTP or SQL requests without having to wrap them up
     * with async runnables.
     *
     * @param playerData Player data to load
     */
    public CompletableFuture<Void> loadData(H playerData);

    public O getOffline(UUID uuid);
}
