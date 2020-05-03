package elements;

import primitives.Color;

/**
 * @author aviya and sima
 */

public class AmbientLight {
    Color _Ia;
    double _Ka;

    /**
     * constructor for Ambient Light that receives two arguments:
     * @param Ia intensity of light
     * @param Ka attenuation factor
     */
    public AmbientLight(Color Ia, double Ka) {
        _Ia = Ia;
        _Ka = Ka;
    }

    /**
     * get intensity of light
     * @return Ia- intensity
     */
    public Color getIntensity() {
        return _Ia;
    }
}