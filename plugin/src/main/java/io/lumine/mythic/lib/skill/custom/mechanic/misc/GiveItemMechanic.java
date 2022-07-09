package io.lumine.mythic.lib.skill.custom.mechanic.misc;

import io.lumine.mythic.lib.api.util.SmartGive;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.Mechanic;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GiveItemMechanic extends Mechanic {
    private final Material material;
    private final int amount;

    public GiveItemMechanic(ConfigObject config) {
        config.validateKeys("type");

        material = Material.valueOf(config.getString("type").toUpperCase().replace("-", "_"));
        amount = config.getInt("amount", 1);
        Validate.isTrue(amount > 0, "Amount must be strictly positive");
    }

    @Override
    public void cast(SkillMetadata meta) {
        new SmartGive(meta.getCaster().getPlayer()).give(new ItemStack(material, amount));
    }
}
