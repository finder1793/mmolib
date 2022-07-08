package io.lumine.mythic.lib.player.particle;

import com.google.gson.JsonObject;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Enough information to fully display one particle.
 * <p>
 * {@link #rOffset} is used to replace the default three Bukkit parameters
 * used to display a pack of particle. Using only one parameter the particles
 * get displayed in a "ball" when considering the infinite norm
 *
 * @author jules
 */
public class ParticleInformation {

    // Unspecific particle data
    private final Particle particle;
    private final int amount;
    private final double rOffset, speed;

    // Colored particle data
    @Nullable
    private final Color color;
    private final float size;

    // TODO block particle data

    public ParticleInformation(Particle particle) {
        this(particle, 1, 0, 0, null, 1);
    }

    public ParticleInformation(Particle particle, int amount, float speed, double rOffset, Color color, float size) {
        this.particle = particle;
        this.amount = amount;
        this.rOffset = rOffset;
        this.speed = speed;

        this.color = color;
        this.size = size;
    }

    /**
     * Used by particle effects and therefore do NOT need the speed,
     * offsets and amount parameters to be read from the config.
     *
     * @param obj Config to read data from
     * @see {@link ParticleEffect}
     */
    public ParticleInformation(ConfigObject obj) {
        particle = Particle.valueOf(obj.getString("name"));
        color = obj.contains("color") ? readColor(obj.getObject("color")) : null;
        size = (float) obj.getDouble("size", 1);

        // Not being used
        rOffset = speed = 0;
        amount = 1;
    }

    /**
     * Used by MMOItems to display projectile (arrows/tridents) particles
     * and therefore require all the parameters input.
     *
     * @param object Json object to read data from
     */
    public ParticleInformation(JsonObject object) {
        particle = Particle.valueOf(object.get("Particle").getAsString());
        amount = object.get("Amount").getAsInt();
        rOffset = object.get("Offset").getAsDouble();

        boolean colored = object.get("Colored").getAsBoolean();
        color = colored ? Color.fromRGB(object.get("Red").getAsInt(), object.get("Green").getAsInt(), object.get("Blue").getAsInt()) : null;
        speed = colored ? 0 : object.get("Speed").getAsFloat();

        // Not being used
        size = 1;
    }

    /**
     * Displays particle with default parameters
     */
    public void display(Location loc) {
        display(loc, amount, rOffset, rOffset, rOffset, speed);
    }

    /**
     * Displays particle at target location and overrides default speed
     */
    public void display(Location loc, double speed) {
        display(loc, amount, rOffset, rOffset, rOffset, speed);
    }

    /**
     * Displays particle at target location by overriding amount, offset and particle speed
     */
    public void display(Location loc, int amount, double x, double y, double z, double speed) {
        if (particle.getDataType() == Particle.DustOptions.class)
            loc.getWorld().spawnParticle(particle, loc, amount, x, y, z, speed, new Particle.DustOptions(Objects.requireNonNull(color, "No color provided"), size));
        else
            loc.getWorld().spawnParticle(particle, loc, amount, x, y, z, speed);
    }

    private Color readColor(ConfigObject obj) {
        return Color.fromRGB(obj.getInt("red"), obj.getInt("green"), obj.getInt("blue"));
    }
}
