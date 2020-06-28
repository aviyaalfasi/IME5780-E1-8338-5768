package elements;

import primitives.Color;
/**
 * Class Light is an abstract class representing a light. All light classes extend it.
 * @author aviya and sima
 */
public abstract class Light {
    protected Color _intensity;

    /**
     * Constructor for light
     * @param _intensity intensity (color) of light
     */
    public Light(Color _intensity) {
        this._intensity = _intensity;
    }

    /**
     *  Get intensity of light
     * @return intensity of light
     */
    public Color getIntensity() {
        return _intensity;
    }
}
