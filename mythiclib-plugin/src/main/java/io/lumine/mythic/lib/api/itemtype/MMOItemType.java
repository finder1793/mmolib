package io.lumine.mythic.lib.api.itemtype;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.item.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MMOItemType implements ItemType {
    private final String type, id;

    public MMOItemType(String type, String id) {
        this.type = Objects.requireNonNull(UtilityMethods.enumName(type), "Type cannot be null");
        this.id = Objects.requireNonNull(UtilityMethods.enumName(id), "ID cannot be null");
    }

    @Override
    public boolean matches(ItemStack stack) {
        NBTItem nbt = NBTItem.get(stack);
        return nbt.getString("MMOITEMS_ITEM_TYPE").equalsIgnoreCase(type)
                && nbt.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(id);
    }

    @Override
    public String display() {
        return type + "." + id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MMOItemType that = (MMOItemType) o;
        return type.equals(that.type) && id.equals(that.id);
    }
}

