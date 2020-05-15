package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Class Triangle is the basic class representing a triangle- extends Polygon class- it is a polygon with three vertices.
 * As a result this class also implements the Geometry interface.
 * @author Aviya and Sima
 */
public final class Triangle extends Polygon {
    /**
     * Triangle constructor that receives the values for three triangle vertices
     *
     * @param _p1 first vertice
     * @param _p2 second vertice
     * @param _p3 third vertice
     */
    public Triangle(Point3D _p1, Point3D _p2, Point3D _p3) {
        super(_p1, _p2, _p3);
    }

    public Triangle(Color _emmission, Point3D _p1, Point3D _p2, Point3D _p3) {
        super(_emmission, _p1, _p2, _p3);
    }

    public Triangle(Color _emmission, Material _material, Point3D _p1, Point3D _p2, Point3D _p3) {
        this(_emmission, _p1, _p2, _p3);
        this._material = _material;
    }

    /**
     * @return string containing triangle details
     */
    @Override
    public String toString() {
        return "Triangle{ super: " + super.toString() + " }";
    }

    /**
     * find all intersections of received ray with the triangle
     * @param ray
     * @return list of GeopPoints- intersection points with the triangle
     */
    @Override
    public List<GeoPoint> findIntsersections(Ray ray) {
        List<GeoPoint> intersections = _plane.findIntsersections(ray);
        if (intersections == null) return null;

        Point3D p0 = ray.getPoint();
        Vector v = ray.getDirection();

        Vector v1 = _vertices.get(0).subtract(p0);
        Vector v2 = _vertices.get(1).subtract(p0);
        Vector v3 = _vertices.get(2).subtract(p0);

        double s1 = v.dotProduct(v1.crossProduct(v2));
        if (isZero(s1)) return null;
        double s2 = v.dotProduct(v2.crossProduct(v3));
        if (isZero(s2)) return null;
        double s3 = v.dotProduct(v3.crossProduct(v1));
        if (isZero(s3)) return null;

        //for GeoPoint
        intersections.get(0).geometry = this;

        return ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) ? intersections : null;

    }
}
