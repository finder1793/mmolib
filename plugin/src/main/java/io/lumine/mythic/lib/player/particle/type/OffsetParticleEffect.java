package io.lumine.mythic.lib.player.particle.type;

import io.lumine.mythic.lib.player.particle.ParticleEffect;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.entity.Player;

public class OffsetParticleEffect extends ParticleEffect {
    private final double speed, h_offset, v_offset, height;
    private final int amount;

    public OffsetParticleEffect(ConfigObject obj) {
        super(obj);

        speed = obj.getDouble("speed");
        height = obj.getDouble("height");
        h_offset = obj.getDouble("horizontal-offset");
        v_offset = obj.getDouble("vertical-offset");
        amount = obj.getInt("amount");
    }

    @Override
    public void tick(Player player) {
        getParticle().display(player.getPlayer().getLocation().add(0, height, 0), amount, h_offset, v_offset, h_offset, speed);
    }
}
