package elements;

import primitives.Color;

public class AmbientLight {
    Color _Ia;
    double _Ka;

    public Color getIntensity() {
        return _Ia;
    }

    public AmbientLight(Color Ia, double Ka) {
        _Ia = Ia;
        _Ka = Ka;
    }
}
