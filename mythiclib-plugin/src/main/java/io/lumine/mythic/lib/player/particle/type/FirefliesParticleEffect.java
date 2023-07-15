package io.lumine.mythic.lib.player.particle.type;

import io.lumine.mythic.lib.player.particle.ParticleEffect;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FirefliesParticleEffect extends ParticleEffect {
    private final double speed, height, radius, r_speed;
    private final int amount;

    private double j = 0;

    public FirefliesParticleEffect(ConfigObject obj) {
        super(obj);

        speed = obj.getDouble("speed");
        height = obj.getDouble("height");
        radius = obj.getDouble("radius");
        r_speed = obj.getDouble("rotation-speed");
        amount = obj.getInt("amount");
    }

    @Override
    public void tick(Player player) {
        Location loc = player.getLocation();
        for (int k = 0; k < amount; k++) {
            double a = j + Math.PI * 2 * k / amount;
            getParticle().display(loc.clone().add(Math.cos(a) * radius, height, Math.sin(a) * radius), speed);
        }

        j += Math.PI / 48 * r_speed;
        j -= j > Math.PI * 2 ? Math.PI * 2 : 0;
    }
}
