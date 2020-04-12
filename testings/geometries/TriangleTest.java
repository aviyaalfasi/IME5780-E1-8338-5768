package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    /**
     * Test method for Triangle getNormal(Point3D point)
     */
    @Test
    void getNormalTest() {
        Triangle t=new Triangle(new Point3D(0.0,3.0,0.0),new Point3D(0.0,0.0,3.0),new Point3D(0.0,-3.0,0.0));

        // ============ Equivalence Partitions Tests ==============
        //there is only one part to test- points that are in the triangle

        assertEquals(t.getNormal(new Point3D(0.0,2.0,1.0)),(new Vector(new Point3D(1.0,0.0,0.0))));
        assertEquals(t.getNormal(new Point3D(0.0,0.0,2.0)),(new Vector(new Point3D(1.0,0.0,0.0))));
    }
}