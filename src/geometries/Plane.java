package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * Class Plane is the basic class representing a plane. It implements the Geometry interface.
 * @author Aviya and Sima
 */
public final class Plane implements Geometry {
    /*
    this class includes a point on the plane and the normal to that plane
     */
    Point3D _point;
    Vector _normal;

    /**
     * Plane constructor that receives 3 points on the plane- it uses them to calculate the normal to that plane
     * @param _p1 point on the plane
     * @param _p2 point on the plane
     * @param _p3 point on the plane
     */
    public Plane(Point3D _p1, Point3D _p2, Point3D _p3)
    {
        Vector vector1 = _p1.subtract(_p2);
        Vector vector2 = _p3.subtract(_p2);
        _normal = vector1.crossProduct(vector2);
        _point = new Point3D(_p1);
    }

    /**
     * Plane constructor
     * @param _normal normal value
     * @param _point point value
     */
    public Plane(Vector _normal, Point3D _point)
    {
        this._point = new Point3D(_point);
        this._normal = new Vector(_normal);
    }

    /**
     * @return _normal the normal to the plane
     */
    public Vector get_normal(){return new Vector(_normal);}


    /**
     * get normal to received point
     */
    @Override
    public Vector getNormal(Point3D point3D) {
        return null;
    }

    /**
     *@return string containing triangle details
     */
    @Override
    public String toString() {
        return "Plane{ normal= "+_normal+" point= "+_point+" }";
    }
}
