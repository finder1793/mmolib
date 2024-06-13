package io.lumine.mythic.lib.util;


import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * A safer version of CompletableFutures for Bukkit. The main
 * issue with CompletableFutures is that you cannot tell which thread
 * will be executing the consumers passed as arguments.
 * <p>
 * This implementation of futures guarantees that provided consumers
 * will be executed either on an async thread or on the main server thread.
 * <p>
 * Any exception called by an async thread will also print its stack trace
 * to the console.
 *
 * @param <T> Parameter of wrapped instance of CompletableFuture
 */
public class BukkitFuture<T> {
    private final Plugin plugin;
    private final CompletableFuture<T> wrapped;

    public BukkitFuture(@NotNull Plugin plugin) {
        this(plugin, new CompletableFuture<>());
    }

    public BukkitFuture(@NotNull Plugin plugin, @NotNull CompletableFuture<T> wrapped) {
        this.plugin = plugin;
        this.wrapped = wrapped;
    }

    @NotNull
    public BukkitFuture<Void> thenAccept(Consumer<T> consumer) {
        return new BukkitFuture<>(plugin, wrapped.thenAccept(Tasks.sync(plugin, consumer)));
    }

    @NotNull
    public BukkitFuture<Void> thenAcceptAsync(Consumer<T> consumer) {
        return new BukkitFuture<>(plugin, wrapped.thenAccept(t -> Tasks.runAsync(plugin, () -> consumer.accept(t))));
    }

    public boolean complete(T t) {
        return wrapped.complete(t);
    }
}
