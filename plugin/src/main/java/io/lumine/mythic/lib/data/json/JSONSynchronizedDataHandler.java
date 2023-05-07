package io.lumine.mythic.lib.data.json;

import com.google.gson.JsonObject;
import io.lumine.mythic.lib.data.OfflineDataHolder;
import io.lumine.mythic.lib.data.SynchronizedDataHandler;
import io.lumine.mythic.lib.data.SynchronizedDataHolder;
import io.lumine.mythic.lib.data.SynchronizedDataManager;
import io.lumine.mythic.lib.util.config.JsonFile;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class JSONSynchronizedDataHandler<H extends SynchronizedDataHolder, O extends OfflineDataHolder> implements SynchronizedDataHandler<H, O> {
    private final Plugin owning;
    private final boolean profilePlugin;

    public JSONSynchronizedDataHandler(Plugin owning) {
        this(owning, false);
    }

    /**
     * @param owning        Plugin saving data
     * @param profilePlugin See {@link SynchronizedDataManager#SynchronizedDataManager(Plugin, SynchronizedDataHandler, boolean)}
     */
    public JSONSynchronizedDataHandler(Plugin owning, boolean profilePlugin) {
        this.owning = Objects.requireNonNull(owning, "Plugin cannot be null");
        this.profilePlugin = profilePlugin;
    }

    @Override
    public void saveData(H playerData, boolean autosave) {
        final JsonFile configFile = getUserFile(playerData);
        saveInObject(playerData, configFile.getContent());
        configFile.save();
    }

    public abstract void saveInObject(H playerData, JsonObject json);

    @Override
    public CompletableFuture<Void> loadData(H playerData) {
        return CompletableFuture.runAsync(() -> {
            try {
                loadFromObject(playerData, getUserFile(playerData).getContent());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public abstract void loadFromObject(H playerData, JsonObject json);

    private JsonFile getUserFile(H data) {
        final UUID effectiveUUID = profilePlugin ? data.getUniqueId() : data.getProfileId();
        return new JsonFile(owning, "/userdata", effectiveUUID.toString());
    }
}
