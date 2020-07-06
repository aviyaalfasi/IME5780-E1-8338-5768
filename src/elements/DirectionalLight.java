package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Class Directional Light represents a light source that shines in a certain direction.
 * Extends Light abstract class and implements LightSource interface.
 * @author aviya and sima
 */

public class DirectionalLight extends Light implements LightSource{
    private Vector _direction;

    /**
     * Constructor for directional light
     * @param _intensity intensity of directional light (color of light)
     * @param _direction direction of light
     */
    public DirectionalLight(Color _intensity, Vector _direction)
    {
        super(_intensity);
        this._direction = new Vector(_direction);
    }

    /**
     * Get intensity of light from light source on point
     * @param  p point
     * @return intensity of light in point
     */
    @Override
    public Color getIntensity(Point3D p) {
        return super.getIntensity();
    }

    /**
     * Get L- the vector from the light source to received point
     * @param p point
     * @return L- the vector from the light source to received point
     */
    @Override
    public Vector getL(Point3D p) {
        return _direction.normalize();
    }

    /**
     * Get distance of point from light source
     * @param point point to get distance from
     * @return distance of point from light source
     */
    @Override
    public double getDistance(Point3D point)
    {
        return Double.POSITIVE_INFINITY;
    }
}
