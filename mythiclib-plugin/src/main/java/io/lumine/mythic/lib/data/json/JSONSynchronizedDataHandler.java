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

    @Override
    public void saveData(@NotNull H playerData, boolean autosave) {
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

    public abstract void loadFromObject(@NotNull H playerData, @NotNull JsonObject json);

    private JsonFile getUserFile(H playerData) {
        return new JsonFile(owning, "/userdata", playerData.getEffectiveId().toString());
    }
}
