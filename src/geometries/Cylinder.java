package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

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

    public double get_height() {
        return _height;
    }

    /**
     * get normal to received point
     */
    @Override
    public Vector getNormal(Point3D point3D) {
        //  return super.getNormal(point3D);
        Point3D o = _axisRay.getPoint();
        Vector v = _axisRay.get_direction();

        // projection of P-O on the ray:
        double t;
        try {
            t = alignZero(point3D.subtract(o).dotProduct(v));
        } catch (IllegalArgumentException e) { // P = O
            return v;
        }

        // if the point is at a base
        if (t == 0 || isZero(_height - t)) // if it's close to 0, we'll get ZERO vector exception
            return v;

        o = o.add(v.scale(t));
        Vector v2=new Vector(point3D.subtract(o));
        return v2.normalized();
    }

    /**
     *@return string containing cylinder details
     */
    @Override
    public String toString() {
        return "Cylinder{ super: "+super.toString()+ "height= "+_height+" }";
    }
}
