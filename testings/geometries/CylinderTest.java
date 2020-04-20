package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Vector;
import primitives.Ray;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    /**
     * Test method for Cylinder getNormal(Point3D point)
     */
    @Test
    void getNormal() {
        Cylinder c = new Cylinder(4.0, new Ray(new Point3D(0.0,0.0,1.0), new Vector(0.0,0.0,2.0)), 8.0);

        // ============ Equivalence Partitions Tests ==============
        //there are 3 parts- points on the side of the cylinder and on its bases

        //part one: on the side of the cylinder
        assertEquals(c.getNormal(new Point3D(4.0,0.0,0.0)),(new Vector(0.8,0.0,0.6)));
        assertEquals(c.getNormal(new Point3D(1.0,-4.0,2.0)),(new Vector(0.19611613513818404, -0.7844645405527362, -0.5883484054145521)));

        //part two: base of the cylinder
        assertEquals(c.getNormal(new Point3D(1.0,2.0,-3.0)),(new Vector(0.08192319205190406, 0.1638463841038081, 0.9830783046228486)));//bottom base

        //part three: bottom base of the cylinder
        assertEquals(c.getNormal(new Point3D(0.0,3.0,5.0)),(new Vector(0.0, 0.0, 2.0)));//top base

        // =============== Boundary Values Tests ==================
        // the boundary values are at the intersections of the bases and the side of the cylinder

        assertEquals(c.getNormal(new Point3D(4.0,0.0,5.0)),(new Vector(0.0, 0.0, 2.0)));
        assertEquals(c.getNormal(new Point3D(0.0,-4.0,-3.0)),(new Vector(0.0, -0.31622776601683794, 0.9486832980505138)));
    }
}