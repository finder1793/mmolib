package io.lumine.mythic.lib.player.modifier;

/**
 * Interface used for player modifiers. A closable
 * player modifier is a modifier that requires some
 * action to be run whenever it is unregistered
 * from the player's modifier map.
 * <p>
 * For instance, abilities running on a timer need
 * to cancel the bukkit task.
 *
 * @author jules
 */
public interface Closeable {

    void close();
}
