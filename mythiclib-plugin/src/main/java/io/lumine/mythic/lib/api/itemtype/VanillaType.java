package io.lumine.mythic.lib.api.itemtype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class VanillaType implements ItemType {
    private final Material material;

    public VanillaType(Material material) {
        this.material = Objects.requireNonNull(material, "Material cannot be null");
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getType() == material;
    }

    @Override
    public String display() {
        return material.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VanillaType that = (VanillaType) o;
        return material == that.material;
    }

    @Override
    public int hashCode() {
        return Objects.hash(material);
    }
}
