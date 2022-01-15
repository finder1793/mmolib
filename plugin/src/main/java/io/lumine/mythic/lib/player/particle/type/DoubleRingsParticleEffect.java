package io.lumine.mythic.lib.player.particle.type;

import io.lumine.mythic.lib.player.particle.ParticleEffect;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DoubleRingsParticleEffect extends ParticleEffect {
    private final double speed, height, radius, r_speed, y_offset;

    private double j = 0;

    public DoubleRingsParticleEffect(ConfigObject obj) {
        super(obj);

        speed = obj.getDouble("speed");
        height = obj.getDouble("height");
        radius = obj.getDouble("radius");
        r_speed = obj.getDouble("rotation-speed");
        y_offset = obj.getDouble("y-offset");
    }

    @Override
    public void tick(Player player) {
        Location loc = player.getLocation();
        for (double k = 0; k < 2; k++) {
            double a = j + k * Math.PI;
            getParticle().display(loc.clone().add(radius * Math.cos(a), height + Math.sin(j) * y_offset, radius * Math.sin(a)), speed);
        }

        j += Math.PI / 16 * r_speed;
        j -= j > Math.PI * 2 ? Math.PI * 2 : 0;
    }
}
