package io.lumine.mythic.lib.util;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Intermediate class between Bukkit vectors and locations.
 * Unlike locations, positions do not save a direction. Unlike
 * vectors, positions do save a world.
 * <p>
 * Unlike locations this class does NOT check if two positions
 * are in the same world before attempting a potentially
 * illegal calculation like adding two positions in different worlds.
 */
public class Position {
    private final World world;

    private double x, y, z;

    private static final double epsilon = 1.0E-6D;

    public Position(World world, double x, double y, double z) {
        this.world = Objects.requireNonNull(world, "World cannot be null");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(Location loc) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
    }

    public Position(World world, Vector vec) {
        this(world, vec.getX(), vec.getY(), vec.getZ());
    }

    @NotNull
    public Position add(@NotNull Position pos) {
        this.x += pos.x;
        this.y += pos.y;
        this.z += pos.z;
        return this;
    }

    @NotNull
    public Position add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @NotNull
    public Position subtract(@NotNull Position pos) {
        this.x -= pos.x;
        this.y -= pos.y;
        this.z -= pos.z;
        return this;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public double distance(@NotNull Position o) {
        return Math.sqrt(NumberConversions.square(this.x - o.x) + NumberConversions.square(this.y - o.y) + NumberConversions.square(this.z - o.z));
    }

    public double distanceSquared(@NotNull Position o) {
        return NumberConversions.square(this.x - o.x) + NumberConversions.square(this.y - o.y) + NumberConversions.square(this.z - o.z);
    }

    public float angle(@NotNull Position other) {
        double dot = Math.max(-1, Math.min(1, this.dot(other) / (this.length() * other.length())));
        return (float) Math.acos(dot);
    }

    @NotNull
    public Position multiply(double m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    public double dot(@NotNull Position other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    @NotNull
    public Position getCrossProduct(@NotNull Position o) {
        double x = this.y * o.z - o.y * this.z;
        double y = this.z * o.x - o.z * this.x;
        double z = this.x * o.y - o.x * this.y;
        return new Position(world, x, y, z);
    }

    @NotNull
    public Position normalize() {
        double length = this.length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    @NotNull
    public Position zero() {
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
        return this;
    }

    public boolean isInAABB(@NotNull Position min, @NotNull Position max) {
        return this.x >= min.x && this.x <= max.x && this.y >= min.y && this.y <= max.y && this.z >= min.z && this.z <= max.z;
    }

    public boolean isInSphere(@NotNull Position origin, double radius) {
        return NumberConversions.square(origin.x - this.x) + NumberConversions.square(origin.y - this.y) + NumberConversions.square(origin.z - this.z) <= NumberConversions.square(radius);
    }

    public boolean isNormalized() {
        return Math.abs(this.lengthSquared() - 1.0D) < epsilon;
    }

    @NotNull
    public Position rotateAroundX(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double y = angleCos * this.getY() - angleSin * this.getZ();
        double z = angleSin * this.getY() + angleCos * this.getZ();
        return this.setY(y).setZ(z);
    }

    @NotNull
    public Position rotateAroundY(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * this.getX() + angleSin * this.getZ();
        double z = -angleSin * this.getX() + angleCos * this.getZ();
        return this.setX(x).setZ(z);
    }

    @NotNull
    public Position rotateAroundZ(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * this.getX() - angleSin * this.getY();
        double y = angleSin * this.getX() + angleCos * this.getY();
        return this.setX(x).setY(y);
    }

    @NotNull
    public Position rotateAroundAxis(@NotNull Position axis, double angle) throws IllegalArgumentException {
        Preconditions.checkArgument(axis != null, "The provided axis vector was null");
        return this.rotateAroundNonUnitAxis(axis.isNormalized() ? axis : axis.clone().normalize(), angle);
    }

    @NotNull
    public Position rotateAroundNonUnitAxis(@NotNull Position axis, double angle) throws IllegalArgumentException {
        Preconditions.checkArgument(axis != null, "The provided axis vector was null");
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        double x2 = axis.getX();
        double y2 = axis.getY();
        double z2 = axis.getZ();
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double dotProduct = this.dot(axis);
        double xPrime = x2 * dotProduct * (1.0D - cosTheta) + x * cosTheta + (-z2 * y + y2 * z) * sinTheta;
        double yPrime = y2 * dotProduct * (1.0D - cosTheta) + y * cosTheta + (z2 * x - x2 * z) * sinTheta;
        double zPrime = z2 * dotProduct * (1.0D - cosTheta) + z * cosTheta + (-y2 * x + x2 * y) * sinTheta;
        return this.setX(xPrime).setY(yPrime).setZ(zPrime);
    }

    public double getX() {
        return this.x;
    }

    public int getBlockX() {
        return NumberConversions.floor(this.x);
    }

    public double getY() {
        return this.y;
    }

    public int getBlockY() {
        return NumberConversions.floor(this.y);
    }

    public double getZ() {
        return this.z;
    }

    public int getBlockZ() {
        return NumberConversions.floor(this.z);
    }

    public World getWorld() {
        return world;
    }

    @NotNull
    public Position setX(double x) {
        this.x = x;
        return this;
    }

    @NotNull
    public Position setY(double y) {
        this.y = y;
        return this;
    }

    @NotNull
    public Position setZ(double z) {
        this.z = z;
        return this;
    }

    public Location toLocation() {
        return new Location(world, x, y, z);
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        } else {
            Position other = (Position) obj;
            return Math.abs(this.x - other.x) < epsilon && Math.abs(this.y - other.y) < epsilon && Math.abs(this.z - other.z) < epsilon;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
        return hash;
    }

    @NotNull
    public Position clone() {
        return new Position(world, x, y, z);
    }

    @Override
    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }

    public void checkFinite() throws IllegalArgumentException {
        NumberConversions.checkFinite(this.x, "x not finite");
        NumberConversions.checkFinite(this.y, "y not finite");
        NumberConversions.checkFinite(this.z, "z not finite");
    }

    public static double getEpsilon() {
        return epsilon;
    }

    @NotNull
    public static Position getMinimum(@NotNull Position v1, @NotNull Position v2) {
        return new Position(v1.world, Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
    }

    @NotNull
    public static Position getMaximum(@NotNull Position v1, @NotNull Position v2) {
        return new Position(v1.world, Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
    }
}
