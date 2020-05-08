package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static primitives.Util.*;

import java.util.*;

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

    @Test
    void findIntersectionsTest(){
        Plane p=new Plane( new Vector(1.0,0.0,0.0),new Point3D(0.0,2.0,2.0));

        // ============ Equivalence Partitions Tests ==============

        //part 1- the ray intersects the plane:
        Ray r=new Ray(new Point3D(4.0,2.0,1.0), new Vector(-1.0,1.0,1.0));
        List<Intersectable.GeoPoint> l=p.findIntsersections(r);
        List<Point3D> expectList=new ArrayList<Point3D>();
        expectList.add(new Point3D(0.0,6.0,5.0));
        assertEquals(expectList,List.of(l.get(0).getPoint()));

        //part 2- the ray does not intersect the plane:
        r=new Ray(new Point3D(4.0,2.0,1.0),new Vector(5.0,-2.0,2.0));
        l=p.findIntsersections(r);
        assertEquals(null,l);

        // =============== Boundary Values Tests ==================

        //ray is parallel to plane:
        //case 1: ray is not included in plane:
        r=new Ray(new Point3D(4.0,-1.0,1.0),new Vector(0.0,2.0,0.0));
        l=p.findIntsersections(r);
        assertEquals(null,l);

        //case 2: ray is included in plane:
        r=new Ray(new Point3D(0.0,-1.0,1.0),new Vector(0.0,2.0,0.0));
        l=p.findIntsersections(r);
        assertEquals(null,l);

        //ray is orthogonal to plane:
        //case 1:

        //case 2:

        //case 3:

        //Ray is neither orthogonal nor parallel to the plane and begins in
        //the same point which appears as reference point in the plane:
        r=new Ray(new Point3D(0.0,2.0,2.0),new Vector(5.0,3.0,1.0));
        l=p.findIntsersections(r);
        assertEquals(null,l);
    }
}





