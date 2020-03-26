package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Class Cylinder is the basic class representing a cylinder. It extends the Tube class.
 * @author Aviya and Sima
 */
public final class Cylinder extends Tube{
    /*
    this class include a radius, an axis ray, and the height of the cylinder
     */
    double _height;

    /**
     * Cylinder constructor
     * @param _radius radius value
     * @param _axisRay axis ray value
     * @param _height height value
     */
    public Cylinder(double _radius, Ray _axisRay, double _height){
        super(_radius,_axisRay);
        this._height=_height;
    }

    /**
     * get normal to received point
     */
    @Override
    public Vector getNormal(Point3D point3D) {
        return super.getNormal(point3D);
    }

    /**
     *@return string containing cylinder details
     */
    @Override
    public String toString() {
        return "Cylinder{ super: "+super.toString()+ "height= "+_height+" }";
    }
}
