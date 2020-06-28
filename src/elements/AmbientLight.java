package elements;

import primitives.Color;

/**
 * Class Ambient Light represents the ambient light in a scene, the basic lighting.
 * @author aviya and sima
 */

public class AmbientLight extends Light {
    /**
     * constructor for Ambient Light
     * @param Ia intensity of light
     * @param Ka attenuation factor
     */
    public AmbientLight(Color Ia, double Ka) {
        super(Ia.scale(Ka));
    }
}