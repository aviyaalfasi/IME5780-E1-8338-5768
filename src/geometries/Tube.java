package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

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
        return null;
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
