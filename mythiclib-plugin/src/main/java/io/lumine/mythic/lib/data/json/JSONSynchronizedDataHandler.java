package io.lumine.mythic.lib.data.json;

import com.google.gson.JsonObject;
import io.lumine.mythic.lib.data.OfflineDataHolder;
import io.lumine.mythic.lib.data.SynchronizedDataHandler;
import io.lumine.mythic.lib.data.SynchronizedDataHolder;
import io.lumine.mythic.lib.util.Jsonable;
import io.lumine.mythic.lib.util.config.JsonFile;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class JSONSynchronizedDataHandler<H extends SynchronizedDataHolder & Jsonable, O extends OfflineDataHolder> implements SynchronizedDataHandler<H, O> {
    private final Plugin owning;

    public JSONSynchronizedDataHandler(Plugin owning) {
        this.owning = Objects.requireNonNull(owning, "Plugin cannot be null");
    }

    @Deprecated
    public JSONSynchronizedDataHandler(Plugin owning, boolean profilePlugin) {
        this(owning);
    }

    /**
     * Override this method if you want to make a mix between profile specific data and profile independent data.
     *
     * @param playerData       The player Data to loadData from
     * @param json             The json corresponding to the effective UUID of the player. (Its profile UUID if MMOProfiles is on)
     * @param officialUUIDJson The json corresponding to the official UUID of the player. (Its real in game UUID)
     *                         If the effective UUID is the same as the official UUID, this json will be the same as the json parameter.
     */
    public void saveInFile(@NotNull H playerData, @NotNull JsonFile json, @NotNull JsonFile officialUUIDJson) {
        json.setContent(playerData.toJson());
    }

    @Override
    public void saveData(@NotNull H playerData, boolean autosave) {
        // TODO json object is uselessly loaded into memory
        final JsonFile file = getUserFile(playerData);
        final JsonFile officialUUIDFile = getEntityUUIDFile(file, playerData);
        saveInFile(playerData, file, officialUUIDFile);
        file.save();
        if (officialUUIDFile != file) {
            officialUUIDFile.setContent(playerData.toJson());
            officialUUIDFile.save();
        }
    }

    @Override
    public boolean loadData(@NotNull H playerData) {
        // TODO support true/false
        final JsonFile file = getUserFile(playerData);
        loadFromObject(playerData, file.getContent(), getEntityUUIDFile(file, playerData).getContent());
        return true;
    }

    public abstract void loadFromObject(@NotNull H playerData, @NotNull JsonObject json);

    /**
     * Override this method if you want to make a mix between profile specific data and profile independent data.
     *
     * @param playerData       The player Data to loadData from
     * @param json             The json corresponding to the effective UUID of the player. (Its profile UUID if MMOProfiles is on)
     * @param entityUUIDJson The json corresponding to the official UUID of the player. (Its real in game UUID)
     *                         If the effective UUID is the same as the official UUID, this json will be the same as the json parameter.
     */
    public void loadFromObject(@NotNull H playerData, @NotNull JsonObject json, @NotNull JsonObject entityUUIDJson) {
        loadFromObject(playerData, json);
    }

    private JsonFile getUserFile(H playerData) {
        return new JsonFile(owning, "/userdata", playerData.getEffectiveId().toString());
    }

    private JsonFile getEntityUUIDFile(JsonFile file, @NotNull H playerData) {
        if (playerData.getEffectiveId().equals(playerData.getUniqueId()))
            return file;
        return new JsonFile(owning, "/userdata", playerData.getUniqueId().toString());
    }
}
