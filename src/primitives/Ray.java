package primitives;

import static primitives.Util.isZero;

/**
 * Class Ray is the basic class representing a ray (a vector that does not start from (0,0,0)).
 * @author Aviya and Sima
 */
public class Ray {
    private static final double DELTA = 0.1;
    /**
     * The point from which the ray starts.
     */
    private final Point3D _point;
    /**
     * The direction of the ray.
     */
    private final Vector _direction;

    /**
     * Constructor for creating a new instance of this class
     * @param point the start of the ray.
     * @param direction the direction of the ray.
     */
    public Ray(Point3D point, Vector direction) {
        _point = new Point3D(point);
        _direction =(direction).normalized();
    }

    /**
     * @param length
     * @return new Point3D
     */
    public Point3D getTargetPoint(double length) {
        return isZero(length ) ? _point : _point.add(_direction.scale(length));
    }


    /**
     * Copy constructor for a deep copy of an Ray object.
     * @param other the object that being copied
     */
    public Ray(Ray other) {
        this._point = new Point3D(other._point);
        this._direction = other._direction.normalized();
    }

    public Ray(Point3D point, Vector direction, Vector normal) {
        //point + normal.scale(±DELTA)
        _direction = new Vector(direction).normalized();

        double nv = normal.dotProduct(direction);

        Vector normalDelta = normal.scale((nv > 0 ? DELTA : -DELTA));
        _point = point.add(normalDelta);
    }

    /**
     *Equals- receives an object and checks whether it is equal to the ray
     *@return true if rays are equal, false if the are not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Ray))
            return false;
        if (this == obj)
            return true;
        Ray other = (Ray)obj;
        return (_point.equals(other._point) &&
                _direction.equals(other._direction));
    }

    /**
     *@return string containing ray details
     */
    @Override
    public String toString() {
        return String.format ("point: " + _point + ", direction: " + _direction);
    }

    /**
     * Getter for the point from which the ray starts.
     * @return A new Point3D that represents the
     * point from which the ray starts.
     */
    public Point3D getPoint() {
        return new Point3D(_point);
    }

    /**
     * Getter for the direction of the ray that is
     * represented by this object.
     * @return A new Vector that represents the
     * direction of the ray that is
     * represented by this object.
     */
    public Vector getDirection() {
        return new Vector(_direction);
    }
}