package geometries;

import primitives.Color;
import primitives.Material;
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
    protected Material _material;
    public abstract Vector getNormal(Point3D point3D);

    public Color getEmissionLight() {
        return _emmission;
    }

    public Geometry(Color emmission, Material _material) {
        _emmission = new Color(emmission);
        this._material = _material;
    }

    public Geometry(Color emmission) {
        this(emmission, new Material(0,0,0));
    }

    public Geometry(){
        this(Color.BLACK, new Material(0,0,0));
    }

    public Material getMaterial() {
        return _material;
    }
}
