package primitives;

import java.util.Objects;

/**
 * Class Vector is the basic class representing a vector.
 * @author Aviya and Sima
 */
public final class Vector {
    /**
     * head of the vector (the tail is (0,0,0))
     */
    Point3D _head;

    /**
     * Vector constructor
     * throw IllegalArgumentException in the case of an attempt to create a zero vector
     * @param _x Vector projection on the X axis
     * @param _y Vector projection on the Y axis
     * @param _z Vector projection on the Z axis
     */
    public Vector(double _x, double _y, double _z)
    {
        if(_x == 0.0 && _y == 0.0 && _z == 0.0)
            throw new IllegalArgumentException("it is impossible to create zero vector");
        this._head = new Point3D(_x, _y, _z);
    }

    /**
     * Vector constructor receiving:
     * throw IllegalArgumentException in the case of an attempt to create a zero vector
     * @param _x Vector X axis coordinate
     * @param _y Vector Y axis coordinate
     * @param _z Vector Z axis coordinate
     */
    public Vector(Coordinate _x, Coordinate _y, Coordinate _z)
    {
        if(_x._coord == 0.0 && _y._coord == 0.0 && _z._coord == 0.0)
            throw new IllegalArgumentException("it is impossible to create zero vector");
        this._head = new Point3D(new Coordinate(_x), new Coordinate(_y), new Coordinate(_z));
    }

    /**
     * Vector constructor receiving a head value
     * throw IllegalArgumentException in the case of an attempt to create a zero vector
     * @param point3D is the head of the vector
     */
    public Vector(Point3D point3D) {
        if(point3D.equals(Point3D.ZERO))
            throw new IllegalArgumentException("it is impossible to create zero vector");
        _head = new Point3D(point3D);
    }

    /**
     * Vector copy constructor- receives another Vector
     * @param other the vector that is copied
     */
    public Vector(Vector other) { this._head = new Point3D(other._head);}

    /**
     * Get head value
     * @return head value
     */
    public Point3D get_head() {
    return new Point3D(this._head);
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
    *@return string containing vector details
     */
    @Override
    public String toString() {
        return "Vector{" + _head + "}";
    }

    /**
     * dot product between two vectors
     * @param other second vector parameter of dot product
     * @return scalar (type: double)
     */
    public double dotProduct(Vector other) {
        return _head._x.get() * other._head._x.get() +
                _head._y.get() * other._head._y.get() +
                _head._z.get() * other._head._z.get();
    }

    /**
     * cross product between two vectors
     * throw IllegalArgumentException in case the result vector is zero vector
     * @param other second vector parameter of cross product
     * @return Normal vector (type: Vector)
     */
    public Vector crossProduct(Vector other) {
        double w1 = this._head._y._coord * other._head._z._coord - this._head._z._coord * other._head._y._coord;
        double w2 = this._head._z._coord * other._head._x._coord - this._head._x._coord * other._head._z._coord;
        double w3 = this._head._x._coord * other._head._y._coord - this._head._y._coord * other._head._x._coord;

        return new Vector(new Point3D(w1, w2, w3));
    }

    /**
     * subtract two vectors
     * throw IllegalArgumentException in case the result vector is zero vector
     * @param vector second vector to subtract
     * @return the result vector
     */
    public Vector subtract(Vector vector)
    {
        try {
            return this._head.subtract(vector._head);
        }catch (IllegalArgumentException ex){
            throw ex;
        }
    }

    /**
     * add two vectors
     * throw IllegalArgumentException in case the result vector is zero vector
     * @param vector second vector to add
     * @return the result vector
     */
    public Vector add(Vector vector)
    {
        try {
            return new Vector(this._head.add(vector));
        }catch (IllegalArgumentException ex){
            throw ex;
        }
    }

    /**
     * A method that calculates the length of a vector squared
     * @return the length of a vector squared
     */
    public double lengthSquared()
    {
        return this._head._x.get() * this._head._x.get()
                + this._head._y.get() * this._head._y.get()
                +this._head._z.get() * this._head._z.get();
    }

    /**
     * A method that calculates the length of a vector
     * @return the length of a vector
     */
    public double length()
    {
        return  Math.sqrt(this.lengthSquared());
    }

    /**
     * A method that receives a vector and normalizes this vector
     * throw ArithmeticException in case the length of the vector is zero
     */
    public void normalize()
    {
        double length = this.length();
        if(length == 0) {
            throw new ArithmeticException("denominator == ");
        }
        this._head = new Point3D(this._head._x.get() / length, this._head._y.get() / length, this._head._z.get() / length);
    }

    /**
     * A method that get a vector and create new normalizes vector
     * throw IllegalArgumentException in case the result vector is zero vector
     * @return new normalizes vector (same direction)
     */
    public Vector normalized()
    {
        try {
            Vector normalizedVector = new Vector(this);
            normalizedVector.normalize();
            return normalizedVector;
        }catch (IllegalArgumentException ex){
            throw ex;
        }
    }

}