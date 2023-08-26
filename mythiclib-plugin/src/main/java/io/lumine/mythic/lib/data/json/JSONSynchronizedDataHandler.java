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
import java.util.UUID;

public abstract class JSONSynchronizedDataHandler<H extends SynchronizedDataHolder & Jsonable, O extends OfflineDataHolder> implements SynchronizedDataHandler<H, O> {
    private final Plugin owning;
    private final boolean profilePlugin;

    public JSONSynchronizedDataHandler(Plugin owning) {
        this(owning, false);
    }

    public JSONSynchronizedDataHandler(Plugin owning, boolean profilePlugin) {
        this.owning = Objects.requireNonNull(owning, "Plugin cannot be null");
        this.profilePlugin = profilePlugin;
    }

    @Override
    public void saveData(H playerData, boolean autosave) {
        // TODO json object is uselessly loaded into memory
        final JsonFile file = getUserFile(playerData);
        file.setContent(playerData.toJson());
        file.save();
    }

    @Override
    public boolean loadData(@NotNull H playerData) {
        // TODO support true/false
        loadFromObject(playerData, getUserFile(playerData).getContent());
        return true;
    }

    public abstract void loadFromObject(H playerData, JsonObject json);

    private JsonFile getUserFile(H data) {
        final UUID effectiveUUID = profilePlugin ? data.getUniqueId() : data.getProfileId();
        return new JsonFile(owning, "/userdata", effectiveUUID.toString());
    }
}
