package geometries;

import primitives.*;

import static primitives.Util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Plane is the basic class representing a plane. It implements the Geometry interface.
 * @author Aviya and Sima
 */
public final class Plane extends Geometry {
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
        Vector vector1 = new Vector(_p1.subtract(_p2));
        Vector vector2 = new Vector(_p2.subtract(_p3));
        _normal = new Vector(vector1.crossProduct(vector2));
        _point = new Point3D(_p1);
    }

    public Plane(Point3D _p1, Point3D _p2, Point3D _p3, Color _emmission)
    {
        super(_emmission);
        Vector vector1 = new Vector(_p1.subtract(_p2));
        Vector vector2 = new Vector(_p2.subtract(_p3));
        _normal = new Vector(vector1.crossProduct(vector2));
        _point = new Point3D(_p1);
    }

    public Plane(Point3D _p1, Point3D _p2, Point3D _p3, Color _emmission, Material _material)
    {
        this(_p1, _p2, _p3, _emmission);
        this._material = _material;
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

    public Plane(Vector _normal, Point3D _point, Color _emmission)
    {
        super(_emmission);
        this._point = new Point3D(_point);
        this._normal = new Vector(_normal);
    }

    public Plane(Vector _normal, Point3D _point, Color _emmission, Material _material)
    {
        this(_normal, _point, _emmission);
        this._material = _material;
    }

    /**
     * @return _normal the normal to the plane
     */
    public Vector getNormal(){return new Vector(_normal);}


    /**
     * get normal to received point
     */
    @Override
    public Vector getNormal(Point3D point3D) {
        return _normal.normalized();
    }

    /**
     *@return string containing triangle details
     */
    @Override
    public String toString() {
        return "Plane{ normal= "+_normal+" point= "+_point+" }";
    }

    @Override
    public List<GeoPoint> findIntsersections(Ray ray) {
        Vector p0Q;
        try {
            p0Q = _point.subtract(ray.getPoint());
        } catch (IllegalArgumentException e) {
            return null; // ray starts from point Q - no intersections
        }

        double nv = _normal.dotProduct(ray.getDirection());
        if (isZero(nv)) // ray is parallel to the plane - no intersections
            return null;

        double t = alignZero(_normal.dotProduct(p0Q) / nv);

        if (t <= 0) {
            return null;
        }

        GeoPoint geo = new GeoPoint(this, ray.getTargetPoint(t));
        return List.of(geo);
    }
}
