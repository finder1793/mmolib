package io.lumine.mythic.lib.util;

public interface Autosaveable extends Closeable {

    /**
     * @param autosave True when autosaving. False when a player logs off,
     *                 resources need to be flushed and the player data must
     *                 be ready for saving.
     */
    public void prepareSaving(boolean autosave);

    public default void close() {
        prepareSaving(false);
    }
}
