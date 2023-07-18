package io.lumine.mythic.lib.player.particle.type;

import io.lumine.mythic.lib.player.particle.ParticleEffect;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AuraParticleEffect extends ParticleEffect {
    private final double speed, height, radius, r_speed, y_offset, y_speed;
    private final int amount;

    private double j = 0;

    public AuraParticleEffect(ConfigObject obj) {
        super(obj);

        speed = obj.getDouble("speed");
        height = obj.getDouble("height");
        radius = obj.getDouble("radius");
        r_speed = obj.getDouble("rotation-speed");
        y_speed = obj.getDouble("y-speed");
        y_offset = obj.getDouble("y-offset");
        amount = obj.getInt("amount");
    }

    @Override
    public void tick(Player player) {
        Location loc = player.getLocation();
        for (int k = 0; k < amount; k++) {
            double a = j + Math.PI * 2 * k / amount;
            getParticle().display(loc.clone().add(Math.cos(a) * radius, Math.sin(j * y_speed * 3) * y_offset + height, Math.sin(a) * radius), speed);
        }

        j += Math.PI / 48 * r_speed;
        j -= j > Math.PI * 2 / y_speed ? Math.PI * 2 / y_speed : 0;
    }
}
