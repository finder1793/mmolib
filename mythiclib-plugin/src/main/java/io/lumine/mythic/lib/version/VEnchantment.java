package io.lumine.mythic.lib.version;

import io.lumine.mythic.lib.UtilityMethods;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum VEnchantment {
    POWER("POWER", "ARROW_DAMAGE"),
    FORTUNE("FORTUNE", "LOOT_BONUS_BLOCKS"),
    UNBREAKING("UNBREAKING", "DURABILITY"),

    ;

    private final Enchantment wrapped;

    VEnchantment(String... candidates) {
        wrapped = UtilityMethods.resolveEnumField(Enchantment::getByName, candidates);
    }

    @NotNull
    public Enchantment get() {
        return wrapped;
    }
}
