package io.lumine.mythic.lib.player.particle;

import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.Objects;

public class ParticleInformation {
    private final Particle particle;
    private final Color color;
    private final float size;

    public ParticleInformation(Particle particle) {
        this(particle, null, 1);
    }

    public ParticleInformation(Particle particle, Color color, float size) {
        this.particle = particle;
        this.color = color;
        this.size = size;
    }

    public ParticleInformation(ConfigObject obj) {
        particle = Particle.valueOf(obj.getString("name"));
        color = obj.contains("color") ? readColor(obj.getObject("color")) : null;
        size = (float) obj.getDouble("size", 1);
    }

    public Particle getParticle() {
        return particle;
    }

    public Color getColor() {
        return color;
    }

    public float getSize() {
        return size;
    }

    public void display(Location loc, double speed) {
        display(loc, 1, 0, 0, 0, speed);
    }

    public void display(Location loc, int amount, double x, double y, double z, double speed) {
        if (particle.getDataType() == Particle.DustOptions.class)
            loc.getWorld().spawnParticle(particle, loc, amount, x, y, z, speed, new Particle.DustOptions(Objects.requireNonNull(color, "No color provided"), size));
        else
            loc.getWorld().spawnParticle(particle, loc, amount, x, y, z, speed);
    }

    private Color readColor(ConfigObject obj) {
        return Color.fromRGB(obj.getInteger("red"), obj.getInteger("green"), obj.getInteger("blue"));
    }
}
