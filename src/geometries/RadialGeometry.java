package geometries;

/**
 * Class Radial Geometry is an abstract class representing a radial geometry. Any geometry that includes a radius will extend this class.
 * The class implements the Geometry interface, so that any class that will extend it will have to implement the interface as well.
 * @author Aviya and Sima
 */
public abstract class RadialGeometry implements Geometry {
    /*
    this class includes a radius
     */
    double _radius;

    /**
     * Radial Geometry constructor
     * @param _radius
     */
    public RadialGeometry(double _radius)
    {
        this._radius = _radius;
    }

    /**
     * @return radius value
     */
    public double get_radius() {
        return _radius;
    }

    /**
     *@return string containing radius value
     */
    @Override
    public String toString() {
        return "radius= "+_radius;
    }
}
