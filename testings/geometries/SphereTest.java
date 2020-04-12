package geometries;

import primitives.Point3D;
import primitives.Vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    /**
     * Test method for Sphere getNormal(Point3D point)
     */
    @Test
    public void getNormalTest() {
        Sphere s1 = new Sphere(4, new Point3D(0.0,0.0,0.0));
        Sphere s2 = new Sphere(1, new Point3D(1.0,1.0,1.0));

        // ============ Equivalence Partitions Tests ==============
        //there is only one part to test- the points that are on the sphere's face

        assertEquals(s1.getNormal(new Point3D(0.0,0.0,4.0)),(new Vector(new Point3D(0.0,0.0,1.0))));
        assertEquals(s1.getNormal(new Point3D(0.0,0.0,-4.0)),(new Vector(new Point3D(0.0,0.0,-1.0))));
        assertEquals(s1.getNormal(new Point3D(0.0,4.0,0.0)),(new Vector(new Point3D(0.0,1.0,0.0))));
        assertEquals(s1.getNormal(new Point3D(0.0,-4.0,0.0)),(new Vector(new Point3D(0.0,-1.0,0.0))));
        assertEquals(s1.getNormal(new Point3D(4.0,0.0,0.0)),(new Vector(new Point3D(1.0,0.0,0.0))));
        assertEquals(s1.getNormal(new Point3D(-4.0,0.0,0.0)),(new Vector(new Point3D(-1.0,0.0,0.0))));

        assertEquals(s2.getNormal(new Point3D(1.0,1.0,0.0)),(new Vector(new Point3D(0.0,0.0,-1.0))));
        assertEquals(s2.getNormal(new Point3D(0.0,1.0,1.0)),(new Vector(new Point3D(-1.0,0.0,0.0))));
        assertEquals(s2.getNormal(new Point3D(1.0,0.0,1.0)),(new Vector(new Point3D(0.0,-1.0,0.0))));
    }
}
