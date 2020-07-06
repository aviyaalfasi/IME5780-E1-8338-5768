package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;

/**
 *Geometry abstract class- all geometries extend this class. This class implements Intersectable interface.
 * @author Aviya and Sima
 */
public abstract class Geometry implements Intersectable {
    /**
     * emission light of geometry
     */
    protected Color _emmission;
    /**
     * material geometry is made of
     */
    protected Material _material;

    /**
     getNormal- allows to find the normal to every point on the geometry. In each geometry the calculation of this normal is different.
     */
    public abstract Vector getNormal(Point3D point3D);

    /**
     * @return emission light of geometry
     */
    public Color getEmissionLight() {
        return _emmission;
    }

    /**
     * constructor that recieves both parameters of Geometry
     * @param emmission the emmission of geometry
     * @param _material the material of geometry
     */
    public Geometry(Color emmission, Material _material) {
        _emmission = new Color(emmission);
        this._material = _material;
    }

    /**
     * constructor that recieves only emission light
     * @param emmission Color the material of geometry
     */
    public Geometry(Color emmission) {
        this(emmission, new Material(0,0,0));
    }

    /**
     * default constructor creates a balck geometry with no emission light
     */
    public Geometry(){
        this(Color.BLACK, new Material(0,0,0));
    }

    /**
     * @return material geometry is made of
     */
    public Material getMaterial() {
        return _material;
    }
}
