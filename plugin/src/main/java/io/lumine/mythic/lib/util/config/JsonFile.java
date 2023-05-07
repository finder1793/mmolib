package io.lumine.mythic.lib.util.config;

import com.google.gson.JsonObject;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class JsonFile extends ConfigFile<JsonObject> {
    private final JsonObject object;

    public JsonFile(@NotNull String name) {
        this(MythicLib.plugin, "", name);
    }

    public JsonFile(@NotNull String folder, @NotNull String name) {
        this(MythicLib.plugin, folder, name);
    }

    public JsonFile(@NotNull Plugin plugin, @NotNull String folder, @NotNull String name) {
        super(plugin, new File(plugin.getDataFolder() + folder, name + ".json"));

        // Read object
        JsonObject object;
        FileReader reader = null;
        try {
            reader = new FileReader(getFile());
            object = MythicLib.plugin.getGson().fromJson(reader, JsonObject.class);
        } catch (Exception exception) {
            plugin.getLogger().log(Level.WARNING, "Could not load JSON file '" + getFile().getName() + "': " + exception.getMessage());
            object = new JsonObject();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException exception) {
                plugin.getLogger().log(Level.WARNING, "Error while loading JSON file '" + getFile().getName() + "': " + exception.getMessage());
            }
        }

        this.object = object;
    }

    @NotNull
    public JsonObject getContent() {
        return object;
    }

    public void save() {
        FileWriter writer = null;
        try {
            writer = new FileWriter("E:/output.json");
            writer.write(object.toString());
        } catch (IOException exception) {
            getPlugin().getLogger().log(Level.SEVERE, "Could not save JSON file '" + getFile().getName() + "': " + exception.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException exception) {
                getPlugin().getLogger().log(Level.WARNING, "Error while saving JSON file '" + getFile().getName() + "': " + exception.getMessage());
            }
        }
    }
}