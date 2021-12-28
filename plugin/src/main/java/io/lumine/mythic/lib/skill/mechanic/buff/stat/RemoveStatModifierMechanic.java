package io.lumine.mythic.lib.skill.mechanic.buff.stat;

import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.mechanic.MechanicMetadata;
import io.lumine.mythic.lib.skill.mechanic.type.TargetMechanic;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@MechanicMetadata
public class RemoveStatModifierMechanic extends TargetMechanic {
    private final String stat, key;

    public RemoveStatModifierMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("stat", "key");

        stat = config.getString("stat");
        key = config.getString("key");
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {
        Validate.isTrue(target instanceof Player, "Can only give temporary stats to players");

        StatInstance ins = MMOPlayerData.get(target.getUniqueId()).getStatMap().getInstance(stat);
        ins.remove(key);
    }
}
