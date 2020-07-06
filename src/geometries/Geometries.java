package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.ArrayList;

import java.util.List;

/**
 * class Geometries is class that represent all the basic geometries
 * @author Aviya and Sima
 */
public class Geometries implements Intersectable {
    private List<Intersectable> _geometries;

    /**
     * constructor
     */
    public Geometries(){
        this._geometries = new ArrayList<Intersectable>();
    }

    /**
     *  parameter constructor
     * @param _geometries geometries collection parameter
     */
    public Geometries(Intersectable... _geometries) {
        add( _geometries);
    }

    /**
     * the function get collection of geometries and add it to the class geometries collection
     * @param geometries collection of geometries
     */
    public void add(Intersectable... geometries) {
        for (Intersectable geometry : geometries ) {
            _geometries.add(geometry);
        }
    }

    /**
     * the function get a ray and returns list of intersection points with all the geometries collection
     * @param ray the ray to find intersections with
     * @return list of Point3D that intersect the osef
     */
    @Override
    public List<GeoPoint> findIntsersections(Ray ray) {
        List<GeoPoint> intersections = null;

        for (Intersectable geometry : _geometries) {
            List<GeoPoint> tempIntersections = geometry.findIntsersections(ray);
            if (tempIntersections != null) {
                if (intersections == null)
                    intersections = new ArrayList<GeoPoint>();
                intersections.addAll(tempIntersections);
            }

        }
        return intersections;
    }
}
