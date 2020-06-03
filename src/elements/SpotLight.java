package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Util;
import primitives.Vector;

import static java.lang.Math.max;

public class SpotLight extends PointLight {
    Vector _direction;
    double _concentration;

    /**
     * spot light constructor
     * @param colorIntensity intensity (color) of light
     * @param position position of light
     * @param direction direction of light
     * @param kC Constant attenuation
     * @param kL Linear attenuation
     * @param kQ Quadratic attenuation
     * @param concentration concentration of light
     */
    public SpotLight(Color colorIntensity, Point3D position, Vector direction, double kC, double kL, double kQ, double concentration) {
        super(colorIntensity, position, kC, kL, kQ);
        this._direction = new Vector(direction).normalized();
        this._concentration = concentration;
    }

    /**
     * spot light constructor
     * @param colorIntensity intensity (color) of light
     * @param position position of light
     * @param direction direction of light
     * @param kC Constant attenuation
     * @param kL Linear attenuation
     * @param kQ Quadratic attenuation
     */
    public SpotLight(Color colorIntensity, Point3D position, Vector direction, double kC, double kL, double kQ) {
        this(colorIntensity, position, direction, kC, kL, kQ, 1);
    }


    /**
     * @return spotlight intensity
     */
    @Override
    public Color getIntensity(Point3D p) {
        double projection = _direction.dotProduct(getL(p));

        if (Util.isZero(projection)) {
            return Color.BLACK;
        }
        double factor = Math.max(0, projection);
        Color pointlightIntensity = super.getIntensity(p);

        if (_concentration != 1) {
            factor = Math.pow(factor, _concentration);
        }

        return (pointlightIntensity.scale(factor));
    }

}

