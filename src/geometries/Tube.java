package geometries;


import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

/**
 * Class Tube is the basic class representing a Tube. This class extends Radial Geometries class, which implements the Geometry interface.
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
     * get normal to received point
     */
    @Override
    public Vector getNormal(Point3D point3D) {
        //The vector from the point of the cylinder to the given point
        Point3D o = _axisRay.get_point0();
        Vector v = _axisRay.get_direction();

        Vector vector1 = new Vector(point3D.subtract(o));

        //We need the projection to multiply the _direction unit vector
        double projection = vector1.dotProduct(v);
        if (!isZero(projection)) {
            // projection of P-O on the ray:
            o.add(v.scale(projection));
        }

        //This vector is orthogonal to the _direction vector.
        Vector check = new Vector(point3D.subtract(o));
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
}
