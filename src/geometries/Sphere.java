package geometries;

import primitives.Point3D;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

import static primitives.Util.*;

/**
 * Class Sphere is the basic class representing a sphere. This class extends Radial Geometries class, which implements the Geometry interface.
 * @author Aviya and Sima
 */

public final class Sphere extends RadialGeometry {
    /*
    this class contains a radius (radial geometry) and a point that is the center of the sphere
     */
    Point3D _center;

    /**
     * Sphere constructor
     * @param _radius sphere radius value
     * @param _center sphere center point value
     */
    public Sphere(double _radius, Point3D _center)
    {
        super(_radius);
        this._center = new Point3D(_center);
    }

    /**
     * get normal to received point
     */
    @Override
    public Vector getNormal(Point3D point3D)
    {
        Vector r=new Vector(point3D.subtract(_center));
        return r.normalized();
    }

    /**
     * Returns a string containing shpere details using Radial Geometry toString method which prints radius value
     *@return string containing sphere details
     */
    @Override
    public String toString() {
        return "Sphere{ "+super.toString()+" center= "+_center+" }";
    }

    @Override
    public List<Point3D> findIntsersections(Ray ray) {
        Point3D p0 = ray.getPoint();
        Vector v = ray.getDirection();
        Vector u;
        try {
            u = _center.subtract(p0);   // p0 == _center
        } catch (IllegalArgumentException e) {
            return List.of(ray.getTargetPoint(_radius));
        }
        double tm = alignZero(v.dotProduct(u));
        double dSquared = (tm == 0) ? u.lengthSquared() : u.lengthSquared() - tm * tm;
        double thSquared = alignZero(_radius * _radius - dSquared);

        if (thSquared <= 0) return null;

        double th = alignZero(Math.sqrt(thSquared));
        if (th == 0) return null;

        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        if (t1 <= 0 && t2 <= 0) return null;
        if (t1 > 0 && t2 > 0) return List.of(ray.getTargetPoint(t1), ray.getTargetPoint(t2)); //P1 , P2
        if (t1 > 0)
            return List.of(ray.getTargetPoint(t1));
        else
            return List.of(ray.getTargetPoint(t2));
    }
}
