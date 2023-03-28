package io.lumine.mythic.lib.api.event.unlocking;

import io.lumine.mythic.lib.api.event.MMOPlayerDataEvent;
import io.lumine.mythic.lib.api.player.MMOPlayerData;

public abstract class ItemChangeEvent extends MMOPlayerDataEvent {
    private final String itemKey;

    public ItemChangeEvent(MMOPlayerData playerData, String itemKey) {
        super(playerData);
        this.itemKey = itemKey;
    }

    /**
     * @return The full item key in the format <plugin-id>:<item-type-id>:<item-id>.
     */
    public String getItemKey() {
        return itemKey;
    }

    /**
     * @return The plugin-id which is the first parameter in the key format <plugin-id>:<item-type-id>:<item-id>.
     *         This is the id of the plugin that made the change and unlocked this item.
     */
    public String getPluginId(){
        return itemKey.split(":")[0];
    }

    /**
     * @return The item-type-id which is the middle parameter in the key format <plugin-id>:<item-type-id>:<item-id>.
     */
    public String getItemTypeId(){
        return itemKey.split(":")[1];
    }

    /**
     * @return The item--id which is the last parameter in the key format <plugin-id>:<item-type-id>:<item-id>.
     */
    public String getItemId(){
        return itemKey.split(":")[2];
    }
}
