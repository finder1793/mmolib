package io.lumine.mythic.lib.player.particle.type;

import io.lumine.mythic.lib.player.particle.ParticleEffect;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HelixParticleEffect extends ParticleEffect {
    private final double speed, height, radius, r_speed, y_speed;
    private final int amount;

    private double j = 0;

    public HelixParticleEffect(ConfigObject obj) {
        super(obj);

        speed = obj.getDouble("speed");
        height = obj.getDouble("height");
        radius = obj.getDouble("radius");
        r_speed = obj.getDouble("rotation-speed");
        y_speed = obj.getDouble("y-speed");
        amount = obj.getInt("amount");
    }

    @Override
    public void tick(Player player) {
        Location loc = player.getLocation();
        for (double k = 0; k < amount; k++) {
            double a = j + k * Math.PI * 2 / amount;
            getParticle().display(loc.clone().add(Math.cos(a) * Math.cos(j * y_speed) * radius, 1 + Math.sin(j * y_speed) * height, Math.sin(a) * Math.cos(j * y_speed) * radius), speed);
        }

        j += Math.PI / 24 * r_speed;
        j -= j > Math.PI * 2 / y_speed ? Math.PI * 2 / y_speed : 0;
    }
}
