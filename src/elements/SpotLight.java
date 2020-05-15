package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import static java.lang.Math.max;

public class SpotLight extends PointLight {
    private Vector _direction;

    public SpotLight(Color _intensity, Point3D _position,Vector _direction, double _kC, double _kL, double _kQ) {
        super(_intensity, _position, _kC, _kL, _kQ);
        this._direction = new Vector(_direction);
    }

    @Override
    public Vector getL(Point3D p) {
        return _direction.normalize();
    }

    public Color getIntensity(Point3D p)
    {
        double _distance=p.distance(_position);
        double m=max(0,_direction.dotProduct(super.getL(p)));
        return _intensity.scale(m/(_kC+_kL*_distance+ kQ *_distance*_distance));

    }

}

