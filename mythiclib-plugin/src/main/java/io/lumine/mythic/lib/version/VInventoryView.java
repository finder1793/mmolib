package io.lumine.mythic.lib.version;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * 1.21 changed InventoryView from abstract class to interface.
 * This class guarantees pre-1.21 backwards compatibility
 */
public interface VInventoryView {

    public String getTitle();

    public InventoryType getType();

    public Inventory getTopInventory();

    public Inventory getBottomInventory();

    void setCursor(ItemStack actualCursor);

    HumanEntity getPlayer();
}
