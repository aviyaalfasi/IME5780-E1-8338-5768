package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Vector;
import primitives.Ray;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    /**
     * Test method for Tube getNormal(Point3D point)
     */
    @Test
    void getNormal() {
        Tube t=new Tube(4,new Ray(new Point3D(0.0,0.0,1.0), new Vector(0.0,0.0,2.0)));
        Tube t2=new Tube(1,new Ray(new Point3D(0.0,1.0,0.0), new Vector(0.0,2.0,0.0)));

        // ============ Equivalence Partitions Tests ==============
        //there is only one part to test- points that are on the tube because it is infinite
        //the tube we are testing has an axis ray that is the z-axis

        assertEquals(t.getNormal(new Point3D(0.0,4.0,0.0)),(new Vector(0.0, 0.9701425001453319, -0.24253562503633297)));
        assertEquals(t.getNormal(new Point3D(-4.0,0.0,0.0)),(new Vector(-0.9701425001453319, 0.0, -0.24253562503633297)));

        assertEquals(t2.getNormal(new Point3D(-1.0,0.0,0.0)),(new Vector(-0.7071067811865475, -0.7071067811865475, 0.0)));
        assertEquals(t2.getNormal(new Point3D(0.0,0.0,1.0)),(new Vector(0.0, -0.7071067811865475, 0.7071067811865475)));
    }
}