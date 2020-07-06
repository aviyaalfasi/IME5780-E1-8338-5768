package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;
/**
 * Light Source is an interface that is implemented only by lights that are light sources.
 * @author aviya and sima
 */
public interface LightSource {
    /**
     * Get intensity of light from light source on point
     * @param p point
     * @return intensity of light from light source on point
     */
    public Color getIntensity(Point3D p);

    /**
     * Get L- the vector from the light source to received point
     * @param p point
     * @return L- the vector from the light source to received point
     */
    public Vector getL(Point3D p);

    /**
     * Get distance from light source to point
     * @param point point to get distance from
     * @return distance from light source to point
     */
    public double getDistance(Point3D point);
}
