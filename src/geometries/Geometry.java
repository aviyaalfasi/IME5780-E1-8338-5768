package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 *Geometry Interface- all geometries implement this interface.
 * @author Aviya and Sima
 */
public interface Geometry {
    /*
    getNormal- allows to find the normal to every point on the geometry. In each geometry the calculation of this normal is different.
     */
    Vector getNormal(Point3D point3D);
}
