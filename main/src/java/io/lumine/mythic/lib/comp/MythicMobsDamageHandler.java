package io.lumine.mythic.lib.comp;

import io.lumine.mythic.lib.api.AttackResult;
import io.lumine.mythic.lib.api.DamageHandler;
import io.lumine.mythic.lib.api.DamageType;
import io.lumine.mythic.lib.api.RegisteredAttack;
import org.bukkit.entity.Entity;

public class MythicMobsDamageHandler implements DamageHandler {

    @Override
    public RegisteredAttack getDamage(Entity entity) {
        return new RegisteredAttack(new AttackResult(0, DamageType.MAGIC), null);
    }

    @Override
    public boolean hasDamage(Entity entity) {
        return entity.hasMetadata("skill-damage");
    }
}
