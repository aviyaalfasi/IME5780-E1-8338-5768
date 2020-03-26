package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * Class Triangle is the basic class representing a triangle- extends Polygon class- it is a polygon with three vertices.
 * As a result this class also implements the Geometry interface.
 * @author Aviya and Sima
 */
public final class Triangle extends Polygon {
    /**
     * Triangle constructor that receives the values for three triangle vertices
     * @param _p1 first vertice
     * @param _p2 second vertice
     * @param _p3 third vertice
     */
    public Triangle(Point3D _p1, Point3D _p2, Point3D _p3)
    {
        super(_p1, _p2, _p3);
    }

    /**
     * get normal to received point
     */
    @Override
    public Vector getNormal(Point3D point) {
        return super.getNormal(point);
    }

    /**
     *@return string containing triangle details
     */
    @Override
    public String toString() {
        return "Triangle{ super: "+super.toString()+" }";
    }
}
