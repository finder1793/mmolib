package io.lumine.mythic.lib.comp;

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
