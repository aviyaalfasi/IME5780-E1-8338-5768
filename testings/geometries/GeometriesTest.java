package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    @Test
    void findIntsersections() {
        Geometries geometries = new Geometries();

        // =============== Boundary Values Tests ==================
        //TC01: empty geometries list
        assertNull(geometries.findIntsersections(new Ray(new Point3D(0.0,1.0,0.0), new Vector(1.0,0.0,5.0))));

        geometries.add(new Plane(new Vector(0.0,0.0,1.0), new Point3D(1.0,1.0,0.0)));
        geometries.add(new Triangle(new Point3D(1.0,0.0,0.0), new Point3D(0.0,1.0,0.0), new Point3D(0.0,0.0,1.0)));
        geometries.add(new Sphere(1.0, new Point3D(1.0, 0.0, 0.0)));

        //TC02: each geometry does'nt have intersection points
        assertNull(geometries.findIntsersections(new Ray(new Point3D(0.0,0.0,2.0), new Vector(0.0,-1.0,0.0))));

        //TC03: just one geometry has intersections point
        assertEquals( 1, geometries.findIntsersections(new Ray(new Point3D(0.0,5.0,-1.0), new Vector(0.0,0.0,1.0))).size());

        //TC04: all of the geometries have intersection points
        Geometries all_geometries = new Geometries();
        all_geometries.add(new Plane(new Vector(0.0,-1.0,0.0),new Point3D(0.0,1.0,2.0)));
        all_geometries.add(new Triangle(new Point3D(0.4,3.0,1.0), new Point3D(0.0,3.0,1.0), new Point3D(2.0,3.0,5.0)));
        all_geometries.add(new Sphere(1.0, new Point3D(0.0,5.0,1.0)));
        assertEquals( 3.0,all_geometries.findIntsersections(new Ray(new Point3D(Point3D.ZERO), new Vector(0.0, 5.0, 1.0))).size());


        // ============ Equivalence Partitions Tests ==============
        //TC11: part of the geometries has intersection points
        assertEquals( 2, geometries.findIntsersections(new Ray(new Point3D(1.0,0.0,-1.0), new Vector(0.0,0.0,1.0))).size());
    }
}