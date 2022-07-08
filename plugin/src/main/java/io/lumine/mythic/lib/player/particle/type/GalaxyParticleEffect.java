package io.lumine.mythic.lib.player.particle.type;

import io.lumine.mythic.lib.player.particle.ParticleEffect;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GalaxyParticleEffect extends ParticleEffect {
    private final double speed, height, r_speed, y_coord;
    private final int amount;

    private double j = 0;

    public GalaxyParticleEffect(ConfigObject obj) {
        super(obj);

        speed = obj.getDouble("speed") * .2;
        height = obj.getDouble("height");
        r_speed = obj.getDouble("rotation-speed");
        y_coord = obj.getDouble("y-coord");
        amount = obj.getInt("amount");
    }

    @Override
    public void tick(Player player) {
        Location loc = player.getLocation();
        for (int k = 0; k < amount; k++) {
            double a = j + Math.PI * 2 * k / amount;
            getParticle().display(loc.clone().add(0, height, 0), 0, (float) Math.cos(a), y_coord, (float) Math.sin(a), speed);
        }

        j += Math.PI / 24 * r_speed;
        j -= j > Math.PI * 2 ? Math.PI * 2 : 0;
    }
}
