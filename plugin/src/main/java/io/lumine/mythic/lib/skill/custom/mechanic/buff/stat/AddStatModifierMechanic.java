package io.lumine.mythic.lib.skill.custom.mechanic.buff.stat;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.api.stat.modifier.TemporaryStatModifier;
import io.lumine.mythic.lib.skill.custom.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.custom.mechanic.MechanicMetadata;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AddStatModifierMechanic extends TargetMechanic {
    private final DoubleFormula amount, lifetime;
    private final String stat, key;
    private final boolean relative;

    @MechanicMetadata
    public AddStatModifierMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("stat", "amount", "key");

        stat = config.getString("stat");
        key = config.getString("key");
        lifetime = config.contains("time") ? new DoubleFormula(config.getString("time")) : DoubleFormula.ZERO;
        relative = config.getBoolean("relative", false);
        amount = new DoubleFormula(config.getString("amount"));
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        Validate.isTrue(target instanceof Player, "Can only give temporary stats to players");

        StatInstance ins = MMOPlayerData.get(target.getUniqueId()).getStatMap().getInstance(stat);
        long lifetime = Math.max(0, (long) this.lifetime.evaluate(meta));

        StatModifier modifier = lifetime > 0 ?

                // This is a temporary stat modifier
                new TemporaryStatModifier(amount.evaluate(meta), lifetime, relative ? ModifierType.RELATIVE : ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER, key, ins) :

                // Permanent stat modifier
                new StatModifier(amount.evaluate(meta), relative ? ModifierType.RELATIVE : ModifierType.FLAT, EquipmentSlot.OTHER, ModifierSource.OTHER);

        ins.addModifier(key, modifier);
    }
}
