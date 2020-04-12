package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    /**
     * Test method for Plane getNormal(Point3D point)
     */
    @Test
    void getNormalTest() {
        Plane p=new Plane( new Vector(1.0,0.0,0.0),new Point3D(0.0,2.0,2.0) );

        // ============ Equivalence Partitions Tests ==============
        //there is only one part to test- points that are on the plane

        assertEquals(p.getNormal(new Point3D(0.0,9.0,4.0)),(new Vector(new Point3D(1.0,0.0,0.0))));
        assertEquals(p.getNormal(new Point3D(0.0,50.0,3.0)),(new Vector(new Point3D(1.0,0.0,0.0))));
        assertEquals(p.getNormal(new Point3D(0.0,7.8,4.0)),(new Vector(new Point3D(1.0,0.0,0.0))));
    }
}




