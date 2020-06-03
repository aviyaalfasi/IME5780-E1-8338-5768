package elements;

import primitives.Color;

public abstract class Light {
    protected Color _intensity;

    /**
     * constructor for light
     * @param _intensity intensity (color) of light
     */
    public Light(Color _intensity) {
        this._intensity = _intensity;
    }

    /**
     * @return intensity of light
     */
    public Color getIntensity() {
        return _intensity;
    }
}
