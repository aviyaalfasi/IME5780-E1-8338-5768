package geometries;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 *Geometry Interface- all geometries implement this interface.
 * @author Aviya and Sima
 */
public abstract class Geometry implements Intersectable {
    /**
    getNormal- allows to find the normal to every point on the geometry. In each geometry the calculation of this normal is different.
     */
    protected Color _emmission;
    public abstract Vector getNormal(Point3D point3D);

    public Color get_emmission() {
        return _emmission;
    }

    public Geometry(Color emmission) {
        _emmission = new Color(emmission);
    }

    public Geometry(){
        this(Color.BLACK);
    }
}
