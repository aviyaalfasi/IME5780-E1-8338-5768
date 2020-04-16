package primitives;

import static primitives.Util.*;
import java.util.Objects;

/**
 * Class Ray is the basic class representing a ray (a vector that does not start from (0,0,0)).
 * @author Aviya and Sima
 */

public final class Ray {
    /*
    Point0- the point where the ray starts
     */
    Point3D _point0;
    /*
    Direction- the direction of the ray
     */
    Vector _direction;

    /**
     * Ray constructor that receives the values for:
     * @param _point0
     * @param _direction
     */
    public Ray(Point3D _point0, Vector _direction)
    {
        this._point0 = new Point3D(_point0);
        this._direction = new Vector(_direction);
    }

    /**
     * Ray copy constructor- receives another Ray
     * @param _other the Ray that is copied
     */
    public Ray(Ray _other)
    {
        this._point0 = new Point3D(_other._point0);
        this._direction = new Vector(_other._direction);
    }

    /**
     * @return The starting point of the Ray - type: Point3D
     */
    public Point3D getPoint() { return new Point3D(_point0); }

    /**
     * @return the direction vector of the ray - type: Vector
     */
    public Vector get_direction() { return new Vector(_direction); }

    /**
     *Equals- receives an object and checks whether it is equal to the ray
     *@return true if rays are equal, false if the are not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return _point0.equals(ray._point0) &&
                _direction.equals(ray._direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_point0, _direction);
    }

    /**
     *@return string containing ray details
     */
    @Override
    public String toString() {
        return "Ray{" +
                "_point0=" + _point0 +
                ", _direction=" + _direction +
                '}';
    }

    /**
     * receives a number and returns the point that is that distance away from the head of the ray
     * @param t
     * @return the point that the distance from it to the head of the ray is t
     */
    public Point3D getPoint(double t)
    {
        if(isZero(t))
            return _point0;
        return _point0.add(_direction.scale(t));
    }

    public Vector getDirection() {
        return new Vector(_direction);
    }

    public Point3D getTargetPoint(double length) {
        return isZero(length ) ? _point0 : _point0.add(_direction.scale(length));
    }
}
