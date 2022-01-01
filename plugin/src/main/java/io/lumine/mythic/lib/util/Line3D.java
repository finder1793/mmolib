package io.lumine.mythic.lib.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Line3D {
    private final Vector direction;
    private final Vector point;

    public Line3D(Vector loc1, Vector loc2) {
        point = loc1.clone();
        direction = loc1.clone().subtract(loc2);
    }

    public Line3D(Location point, Vector direction) {
        this.direction = direction.clone();
        this.point = point.toVector();
    }

    public Line3D(double a, double b, double c, double e, double f, double g) {
        point = new Vector(a, b, c);
        direction = new Vector(e, f, g);
    }

    public double distanceSquared(Entity entity) {
        return distanceSquared(entity.getLocation().add(0, entity.getHeight() / 2, 0).toVector());
    }

    /**
     * Using the quadratic formula to project a point
     * onto an axis and then taking the vector length
     * to calculate the distance from this point to the axis.
     * <p>
     * Let (e, f, g) be the point off the axis of which we
     * are calculating the distance from the axis
     * <p>
     * The parametric equation of the line is the following:
     * point = someAxisPoint + t * vector
     * (x, y, z) = (a, b, c) + t * (alpha, beta, gamma)
     * <p>
     * The "distance" vector is:
     * (a + t * alpha - e, b + t * beta - f, c + t * gamma - g)
     * <p>
     * The length of that vector is a second degree polynomial
     * of the variable t. We're looking for its global minimum
     * and the general value for the extrema is given by the
     * canonical formula: - b / (2 * a)
     * <p>
     * This is the value of t for which the distance is the smallest.
     * Then calculate the corresponding point and calculate its
     * distance from the initial point.
     *
     * @param loc
     * @return
     */
    public double distanceSquared(Vector loc) {

        double a = direction.lengthSquared();
        double b = 2 * (direction.getX() * (point.getX() - loc.getX()) + direction.getY() * (point.getY() - loc.getY()) + direction.getZ() * (point.getZ() - loc.getZ()));
        double min = -b / (2 * a);

        return loc.distanceSquared(getPoint(min));
    }

    public double distance(Vector loc) {
        return Math.sqrt(distanceSquared(loc));
    }

    public Vector getPoint(double t) {
        return point.clone().add(direction.clone().multiply(t));
    }
}
