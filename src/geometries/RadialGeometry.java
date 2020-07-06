package geometries;

import primitives.Color;

/**
 * Class Radial Geometry is an abstract class representing a radial geometry. Any geometry that includes a radius will extend this class.
 * The class extends the Geometry class.
 * @author Aviya and Sima
 */
public abstract class RadialGeometry extends Geometry {
    /*
    this class includes a radius
     */
    double _radius;

    /**
     * Radial Geometry constructor
     * @param _radius radius of radial geometry
     */
    public RadialGeometry(double _radius)
    {
        this._radius = _radius;
    }

    public RadialGeometry(double _radius, Color _emmission) {
        super(_emmission);
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
