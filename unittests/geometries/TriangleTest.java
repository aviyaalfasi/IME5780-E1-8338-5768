package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
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

    @Test
    void findIntersectionsTest()
    {
        Triangle t=new Triangle(new Point3D(1.0,3.0,5.0),new Point3D(5.0,3.0,1.0),new Point3D(0.0,3.0,1.0));

        // ============ Equivalence Partitions Tests ==============

        //case 1- ray intersects with triangle
        Ray r=new Ray(new Point3D(1.0,-5.0,4.0),new Vector(0.0,3.0,0.0));
        List<Intersectable.GeoPoint> l=t.findIntsersections(r);
        assertEquals(new Point3D(1.0,3.0,4.0),l.get(0).getPoint());

        //case 2- ray intersects with plane but outside the triangle against edge
        r=new Ray(new Point3D(1.0,-5.0,4.0),new Vector(3.0,0.0,-1.0));
        l=t.findIntsersections(r);
        assertEquals(null,l);

        //case 3- ray intersects with plane but outside the triangle against vertex
        r=new Ray(new Point3D(1.0,-5.0,4.0),new Vector(1.0,3.0,6.0));
        l=t.findIntsersections(r);
        assertEquals(null,l);

        // =============== Boundary Values Tests ==================

        //case 1- the ray begins before the plane on the edge of triangle
        r=new Ray(new Point3D(4.0,2.0,1.0),new Vector(0.0,1.0,0.0));
        l=t.findIntsersections(r);
        assertEquals(null,l);

        //case 2- the ray begins before the plane on vertex
        r=new Ray(new Point3D(1.0,2.0,5.0),new Vector(0.0,1.0,0.0));
        l=t.findIntsersections(r);
        assertEquals(null,l);

        //case 3- the ray begins before the plane on edge's continuation
        r=new Ray(new Point3D(8.0,2.0,1.0),new Vector(0.0,1.0,0.0));
        l=t.findIntsersections(r);
        assertEquals(null,l);
    }
}