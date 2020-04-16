package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

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
     * @return string containing triangle details
     */
    @Override
    public String toString() {
        return "Triangle{ super: " + super.toString() + " }";
    }

    @Override
    public List<Point3D> findIntsersections(Ray ray) {
        List<Point3D> intersections = _plane.findIntsersections(ray);

        if (intersections == null)
            return null;

        Point3D p0 = ray.get_point0();
        Vector v = ray.get_direction();

        Vector v1 = _vertices.get(0).subtract(p0);
        Vector v2 = _vertices.get(1).subtract(p0);
        Vector v3 = _vertices.get(2).subtract(p0);

        double dp1 = v.dotProduct(v1.crossProduct(v2));
        if (isZero(dp1)) return null;
        double dp2 = v.dotProduct(v2.crossProduct(v3));
        if (isZero(dp2)) return null;
        double dp3 = v.dotProduct(v3.crossProduct(v1));
        if (isZero(dp3)) return null;

        return ((dp1 > 0 && dp2 > 0 && dp3 > 0) || (dp1 < 0 && dp2 < 0 && dp3 < 0)) ? intersections : null;
    }
}
