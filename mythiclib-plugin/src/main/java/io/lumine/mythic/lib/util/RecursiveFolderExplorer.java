package io.lumine.mythic.lib.util;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.logging.Level;

public class RecursiveFolderExplorer {
    private final Consumer<File> action;
    private final Consumer<RuntimeException> exceptionHandling;

    /**
     * Used when a plugin loads things like skills or quests (MMOCore) on startup
     *
     * @param action         Action performed for every file in the given directory
     * @param plugin         Plugin used to register the error log
     * @param errorLogPrefix The beginning of the error log that will be sent to the console
     */
    public RecursiveFolderExplorer(Consumer<File> action, Plugin plugin, String errorLogPrefix) {
        this.action = action;
        this.exceptionHandling = exception -> plugin.getLogger().log(Level.WARNING, errorLogPrefix + ": " + exception.getMessage());
    }

    /**
     * @param action            Action performed for every file in the given directory
     * @param exceptionHandling What happens when an exception is caught
     */
    public RecursiveFolderExplorer(Consumer<File> action, Consumer<RuntimeException> exceptionHandling) {
        this.action = action;
        this.exceptionHandling = exceptionHandling;
    }

    /**
     * This recursively goes through all directories, subdirectories
     * under the given folder and calls the consumer for every file.
     *
     * @param file Either a file or a folder. Both are handled
     */
    public void explore(File file) {
        if (file.isDirectory())
            Arrays.asList(file.listFiles()).forEach(this::explore);
        else
            try {
                action.accept(file);
            } catch (RuntimeException exception) {
                exceptionHandling.accept(exception);
            }
    }
}
