package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class PointLight extends Light implements LightSource{
    Point3D _position;
    double _kC; // Constant attenuation
    double _kL; // Linear attenuation
    double _kQ; // Quadratic attenuation

    public PointLight(Color colorIntensity, Point3D position, double kC, double kL, double kQ) {
        super(colorIntensity);
        this._position = new Point3D(position);
        this._kC = kC;
        this._kL = kL;
        this._kQ = kQ;
    }

    // by default, the constant attenuation value is 1 and the other two values are 0
    public PointLight(Color colorIntensity, Point3D position) {
        this(colorIntensity, position, 1d, 0d, 0d);
    }

    //dummy overriding Light getIntensity()
    @Override
    public Color getIntensity() {
        return super.getIntensity();
    }

    //overriding LightSource getIntensity(Point3D)
    @Override
    public Color getIntensity(Point3D p) {
        double dsquared = p.distanceSquared(_position);
        double d = p.distance(_position);

        return (_intensity.reduce(_kC + _kL * d + _kQ * dsquared));
    }

    // Light vector
    @Override
    public Vector getL(Point3D p) {
        if (p.equals(_position)) {
            return null;
        }
        return p.subtract(_position).normalize();
    }

    @Override
    public double getDistance(Point3D point) {
        return _position.distance(point);
    }
}
