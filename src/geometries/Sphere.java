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
        return null;
    }
}
