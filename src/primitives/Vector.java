package primitives;

import java.nio.charset.CoderResult;
import java.util.Objects;

/**
 * Class Vector is the basic class representing a vector.
 * @author Aviya and Sima
 */
public class Vector {
    /**
     * head of the vector (the tail is (0,0,0))
     */
    Point3D _head;

    /**
     * Vector constructor receiving a head value
     * throw IllegalArgumentException in the case of an attempt to create a zero vector
     * @param p is the head of the vector
     */
    public Vector(Point3D p) {
        if (p.equals(Point3D.ZERO)) { //its impossible to create zero vector
            throw new IllegalArgumentException("Point3D(0.0,0.0,0.0) not valid for vector head");
        }
        this._head = new Point3D(p._x._coord, p._y._coord, p._z._coord);
    }

    /**
     * Vector copy constructor- receives another Vector
     * @param v the vector that is copied
     */
    public Vector(Vector v) {
        this(v._head);
    }

    /**
     * * Vector constructor get two points and create a vector
     * * throw IllegalArgumentException in the case of an attempt to create a zero vector
     * @param p1
     * @param p2
     */
    public Vector(Point3D p1, Point3D p2) {
        this(p1.subtract(p2));
    }

    /**
     * Vector constructor
     * throw IllegalArgumentException in the case of an attempt to create a zero vector
     * @param x Vector projection on the X axis
     * @param y Vector projection on the Y axis
     * @param z Vector projection on the Z axis
     */
    public Vector(double x,double y, double z) {
        this(new Point3D(x,y,z));
    }

    /**
     * Get head value
     * @return head value
     */
    public Point3D get_head() {
        return new Point3D(_head._x._coord, _head._y._coord, _head._z._coord);
    }

    /**
     * add two vectors
     * throw IllegalArgumentException in case the result vector is zero vector
     * @param vector second vector to add
     * @return the result vector
     */
    public Vector  add(Vector vector)
    {
        return  new Vector(this._head.add(vector));
    }

    /**
     * subtract two vectors
     * throw IllegalArgumentException in case the result vector is zero vector
     * @param vector second vector to subtract
     * @return the result vector
     */
    public Vector subtract(Vector vector) {
        return  this._head.subtract(vector._head);
    }

    /**
     *Vector multiplication with scalar
     * @param scalingFacor the scalar number
     * @return new vector
     */
    public Vector scale(double scalingFacor) {
        return new Vector(
                new Point3D( //multiplication the point coordinates with scalar
                        new Coordinate(scalingFacor * _head._x._coord),
                        new Coordinate(scalingFacor * _head._y._coord),
                        new Coordinate(scalingFacor * _head._z._coord)));
    }

    /*
     *Equals- receives an object and checks whether it is equal to the vector
     *@return true if vectors are equal, false if the are not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return _head.equals(vector._head);
    }

    /**
     * dot product between two vectors
     * @param v second vector parameter of dot product
     * @return scalar (type: double)
     */

    public double dotProduct(Vector v) {
        return (this._head._x._coord * v._head._x._coord +
                this._head._y._coord * v._head._y._coord +
                this._head._z._coord * v._head._z._coord);
    }

    /**
     * cross product between two vectors
     * throw IllegalArgumentException in case the result vector is zero vector
     * @param v second vector parameter of cross product
     * @return Normal vector (type: Vector)
     */
    public Vector crossProduct(Vector v) {
        double w1 = this._head._y._coord * v._head._z._coord - this._head._z._coord * v._head._y._coord;
        double w2 = this._head._z._coord * v._head._x._coord - this._head._x._coord * v._head._z._coord;
        double w3 = this._head._x._coord * v._head._y._coord - this._head._y._coord * v._head._x._coord;

        return new Vector(new Point3D(w1, w2, w3));
    }

    /**
     * A method that calculates the length of a vector squared
     * @return the length of a vector squared
     */
    public double lengthSquared() {
        double xx = this._head._x._coord * this._head._x._coord;
        double yy = this._head._y._coord * this._head._y._coord;
        double zz = this._head._z._coord * this._head._z._coord;

        return xx + yy + zz;

    }

    /**
     * A method that calculates the length of a vector
     * @return the length of a vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * A method that receives a vector and normalizes this vector
     * throw ArithmeticException in case the length of the vector is zero
     * @return new normalizes vector
     */
    public Vector normalize() {

        double x = this._head._x._coord;
        double y = this._head._y._coord;
        double z = this._head._z._coord;

        double length = this.length();

        if (length == 0) //in case divide by Zero
            throw new ArithmeticException("divide by Zero");

        //The difference of coordinates in vector length
        this._head._x = new Coordinate(x / length);
        this._head._y = new Coordinate(y / length);
        this._head._z = new Coordinate(z / length);

        return this;
    }

    /**
     * A method that get a vector and create new normalizes vector
     * throw IllegalArgumentException in case the result vector is zero vector
     * @return new normalizes vector (same direction)
     */
    public Vector normalized() {
        Vector vector = new Vector(this);
        vector.normalize();
        return vector;
    }

    /**
     *@return string containing vector details
     */
    @Override
    public String toString() {
        return  _head .toString();
    }
}