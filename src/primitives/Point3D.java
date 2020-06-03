package primitives;

import java.util.Objects;


/**
 *Class Point3D is the basic class representing a 3-Dimensional point.
 * @author Aviya and Sima
 */
public class Point3D {
    /**
     * 3D point includes 3 coordinates:
     * x axis coordinate
     * y axis coordinate
     * z axis coordinate
     */
    Coordinate _x;
    Coordinate _y;
    Coordinate _z;
    public final static Point3D ZERO = new Point3D(0.0, 0.0, 0.0);

    /**
     * Point3D constructor that receives the values for the coordinates as coordinate
     * @param _x  axis coordinate value
     * @param _y  axis coordinate value
     * @param _z  axis coordinate value
     */
    public Point3D(Coordinate _x, Coordinate _y, Coordinate _z) {
        this._x = _x;
        this._y = _y;
        this._z = _z;
    }

    /**
     * Point3D copy constructor- receives another Point3D
     * @param p the point that is copied
     */
    public Point3D(Point3D p) {
        this._x = new Coordinate(p._x);
        this._y = new Coordinate(p._y);
        this._z = new Coordinate(p._z);
    }

    /**
     * Point3D constructor that receives the values for the coordinates as double
     * @param _x x axis coordinate value
     * @param _y y axis coordinate value
     * @param _z z axis coordinate value
     */
    public Point3D(double _x, double _y, double _z) {
        this(new Coordinate(_x), new Coordinate(_y), new Coordinate(_z));
    }

    /******getters******/

    /**
     * @return x axis coordinate value
     */
    public Coordinate get_x() {
        return new Coordinate(_x);
    }

    /**
     * @return y axis coordinate value
     */
    public Coordinate get_y() {
        return new Coordinate(_y);
    }

    /**
     * @return z axis coordinate value
     */
    public Coordinate get_z() {
        return new Coordinate(_z);
    }

    /**
     * Calculates the distance between two points squares
     * @param other the second point
     * @return the distance between the two points squares
     */
    public double distanceSquared(Point3D other)
    {
        return ( (other._x._coord - this._x._coord) * (other._x._coord - this._x._coord) +
                (other._y._coord - this._y._coord) * (other._y._coord - this._y._coord) +
                (other._z._coord - this._z._coord) * (other._z._coord - this._z._coord));
    }

    /**
     * Calculates the distance between two points
     * @param other the second point
     * @return the distance between the two points squares
     */
    public double distance (Point3D other){
        return Math.sqrt(distanceSquared(other));
    }

    /**
     * add vector to point
     * @param v the vector to add
     * @return A new point created in the distance and in the direction of Hector from the original point
     */
    public Point3D add(Vector v) {
        return new Point3D(this._x._coord + v._head._x._coord,
                this._y._coord + v._head._y._coord,
                this._z._coord + v._head._z._coord);
    }

    /**
     * subtract point and vector
     * @param v vector to add
     * @return point3D
     */
    public Point3D subtract(Vector v) {
        return new Point3D(this._x._coord - v._head._x._coord,
                this._y._coord - v._head._y._coord,
                this._z._coord - v._head._z._coord);
    }

    /**
     * subtract two Points3D
     * throw IllegalArgumentException in case the result vector is zero vector
     * @param p second point parameter of subtract
     * @return Vector
     */
    public Vector subtract(Point3D p) {
        return new Vector(new Point3D(
                this._x._coord - p._x._coord,
                this._y._coord - p._y._coord,
                this._z._coord - p._z._coord));
    }

    /*
     *Equals- receives an object and checks whether it is equal to the point3D
     *@return true if points are equal, false if the are not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point3D = (Point3D) o;
        return _x.equals(point3D._x) &&
                _y.equals(point3D._y) &&
                _z.equals(point3D._z);
    }

    /*
     *@return string containing 3-dimensional point details
     */
    @Override
    public String toString() {
        return "(" +
                _x +
                ", " + _y +
                ", " + _z +
                ')';
    }


}