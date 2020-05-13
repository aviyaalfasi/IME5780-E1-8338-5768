package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class DirectionalLight extends Light implements LightSource{
    private Vector _direction;

    public DirectionalLight(Color _intensity, Vector _direction)
    {
        super(_intensity);
        this._direction = new Vector(_direction);
    }

    @Override
    public Color getIntensity(Point3D p) {
        return super.getIntensity();
    }

    @Override
    public Vector getL(Point3D p) {
        return _direction.normalize();
    }
}