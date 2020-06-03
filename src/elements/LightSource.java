package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public interface LightSource {
    /**
     * @param p point
     * @return intensity of light from light source on point
     */
    public Color getIntensity(Point3D p);

    /**
     * @param p point
     * @return L- the vector from the light source to received point
     */
    public Vector getL(Point3D p);

    /**
     * @param point
     * @return distance from light source to point
     */
    public double getDistance(Point3D point);
}
