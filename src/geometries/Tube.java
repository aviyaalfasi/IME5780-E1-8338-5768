package geometries;


import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Class Tube is the basic class representing a Tube. This class extends Radial Geometries class.
 * @author Aviya and Sima
 */
public class Tube extends RadialGeometry {
    /*
    this class includes a radius (radial geometry) and an axis ray
     */
    Ray _axisRay;

    /**
     * Tube constructor
     * @param _radius tube radius value
     * @param _axisRay tube axis ray value
     */
    public Tube(double _radius, Ray _axisRay)
    {
        super(_radius);
        this._axisRay = new Ray(_axisRay);
    }

    /**
     * Tube constructor
     * @param _radius tube radius value
     * @param _axisRay tube axis ray value
     * @param _emmission emmission light of tube
     */
    public Tube(double _radius, Ray _axisRay, Color _emmission){
        super(_radius, _emmission);
        this._axisRay = new Ray(_axisRay);

    }

    /**
     * Tube constructor
     * @param _radius tube radius value
     * @param _axisRay tube axis ray value
     * @param _emmission emmission light of tube
     * @param _material material of tube
     */
    public Tube(double _radius, Ray _axisRay, Color _emmission, Material _material){
        this(_radius,_axisRay, _emmission);
        this._material = _material;
    }
    /**
     * get normal to received point
     */
    @Override
    public Vector getNormal(Point3D point3D) {
        //The vector from the point of the cylinder to the given point
        Point3D o = _axisRay.getPoint();
        Vector v = _axisRay.getDirection();

        Vector vector1 = (point3D.subtract(o));

        //We need the projection to multiply the _direction unit vector
        double projection = vector1.dotProduct(v);
        if (!isZero(projection)) {
            // projection of P-O on the ray:
            o.add(v.scale(projection));
        }

        //This vector is orthogonal to the _direction vector.
        Vector check = (point3D.subtract(o));
        return check.normalized();
    }


    /**
     * Returns a string containing tube details using Radial Geometry toString method which prints radius value
     *@return string containing tube details
     */
    @Override
    public String toString() {
        return "Tube{ "+super.toString()+ " axis ray= "+_axisRay+" }";
    }

    @Override
    public List<GeoPoint> findIntsersections(Ray ray) {
        return null;
    }
}
