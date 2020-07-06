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

    /**
     * Triangle constructor with basic color
     * @param _emmission basic color
     * @param _p1 first vertice
     * @param _p2 second vertice
     * @param _p3 third vertice
     */
    public Triangle(Color _emmission, Point3D _p1, Point3D _p2, Point3D _p3) {
        super(_emmission, _p1, _p2, _p3);
    }

    /**
     * Triangle constructor with basic color and normal
     * @param _emmission basic color
     * @param _material material of triangle
     * @param _p1 first vertice
     * @param _p2 second vertice
     * @param _p3 third vertice
     */
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
     * @param ray ray to find intersections with
     * @return list of GeopPoints- intersection points with the triangle
     */
    @Override
    public List<GeoPoint> findIntsersections(Ray ray) {
        List<GeoPoint> intersections = _plane.findIntsersections(ray);
        if (intersections == null) return null;//there are no intersection points

        Point3D p0 = ray.getPoint();//the start ray point
        Vector v = ray.getDirection();

        Vector v1 = _vertices.get(0).subtract(p0);//vector from the ray start point to the polygon vertices
        Vector v2 = _vertices.get(1).subtract(p0);//vector from the ray start point to the polygon vertices
        Vector v3 = _vertices.get(2).subtract(p0);//vector from the ray start point to the polygon vertices

        double s1 = v.dotProduct(v1.crossProduct(v2));
        if (isZero(s1)) return null;//the point is out of triangle
        double s2 = v.dotProduct(v2.crossProduct(v3));
        if (isZero(s2)) return null;//the point is out of triangle
        double s3 = v.dotProduct(v3.crossProduct(v1));
        if (isZero(s3)) return null;//the point is out of triangle

        //for GeoPoint
        intersections.get(0).geometry = this;

        return ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) ? intersections : null;

    }
}
