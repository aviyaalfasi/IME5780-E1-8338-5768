package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {

    @Test
    void subtract() {
        Point3D p1 = new Point3D(4.0,5.0,6.0);
        Point3D p2 = new Point3D(3.0,2.0,1.0);
        Point3D p3 = new Point3D(1.0,-1.0,1.0);
        Point3D p4 = new Point3D(-1.0,-1.0,-1.0);
        Point3D p5 = new Point3D(Point3D.ZERO);
        Point3D p6 = new Point3D(-2.0, 1.0, 1.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Two vector and point are in the positive quarter
        assertEquals(new Vector(1.0,3.0, 5.0),p1.subtract(p2));
        // TC02: Two vectors are in the positive quarter, check the different direction
        assertEquals(p1.subtract(p2).scale(-1),p2.subtract(p1));

        // TC03: One vector in the positive quarter of x, y, z and the other vector in the negative quarter of x, y, z
        assertEquals(new Vector(5.0, 6.0, 7.0 ) ,p1.subtract(p4));
        //TC04: One vector in the positive quarter of x, y, z and the other vector in the negative quarter of x, y, z
        //check the different direction
        assertEquals(p1.subtract(p4).scale(-1) ,p4.subtract(p1));

        // TC03: One vector in the positive quarter of x and z, and the negative quarter of Y,
        // one vector in the positive quarter of Y and Z and the positive quarter of X
        assertEquals(new Vector(3.0, -2.0, 0.0 ) ,p3.subtract(p6));

    }

    @Test
    void add() {
        Vector v1 = new Vector(1.0, 1.0, 1.0);
        Vector v2 = new Vector(-1.0, -1.0,-1.0);
        Point3D p1 = new Point3D(3.0, 2.0, 1.0);
        Point3D p2 = new Point3D(-5.0,-6.0,-8.0);
        Point3D p3 = new Point3D(-1, -1, -1);
        Point3D p4 = new Point3D(1, 1, 1);

        // ============ Equivalence Partitions Tests ==============

        // TC01: The point above the vector is both in the positive quadrant of X, Y, Z
        assertEquals(new Point3D(4.0,3.0,2.0), p1.add(v1));

        // TC02: The point above the vector, the point in the positive quarter of X, Y, Z and the vector in the negative quarter of X, Y, Z
        assertEquals(new Point3D(2.0, 1.0, 0.0), p1.add(v2));

        // TC03: The point below the vector, the point in the negative quarter of X, Y, Z and the vector in the positive quarter of X, Y, Z
        assertEquals(new Point3D(-4.0, -5.0, -7.0), p2.add(v1));

        // TC04: The point is equal to the edge of the vector, the point in the negative quarter of X, Y, Z and the vector in the negative quarter of X, Y, Z
        assertEquals(new Point3D(-2.0,-2.0,-2.0), p3.add(v2));

        // TC05: The point above the vector, the point in the positive quarter of X, Y, Z and the vector in the negative quarter of X, Y, Z
        assertEquals(new Point3D(0.0,0.0,0.0), p4.add(v2));
    }

    @Test
    void distanceSquared() {
        Point3D p1 = new Point3D(3.0, 2.0, 1.0);
        Point3D p2 = new Point3D(-5.0,-6.0,-8.0);
        Point3D p3 = new Point3D(-1.0, -1.0, -1.0);
        Point3D p4 = new Point3D(1.0, 1.0, 1.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Square distance from a point to itself
        assertEquals(0.0, p3.distanceSquared(p3));

        // TC02: Distance between a point that is in the positive quadrant of X, Y, Z, and a point that is in the negative quadrant of X, Y, Z
        assertEquals(12.0, p4.distanceSquared(p3));

        // TC03: Distance between a point that is in the negative quarter of X, Y, Z, and a point that is in the positive quarter of X, Y, Z
        assertEquals(209.0, p2.distanceSquared(p1));

        // TC04: Tester that the distance between point P1 and P2 equals the distance between point P2 and P1
        assertEquals(p2.distanceSquared(p1), p1.distanceSquared(p2));

        // TC05: Distance between two points, both of which are in the negative quarter of X, Y, Z
        assertEquals(90.0, p2.distanceSquared(p3));

        // TC06: Distance between two points, both of which are in the positive quadrant of X, Y, Z
        assertEquals(5.0, p1.distanceSquared(p4));
    }

    @Test
    void distance() {
        Point3D p1 = new Point3D(3.0, 2.0, 1.0);
        Point3D p2 = new Point3D(-5.0,-6.0,-8.0);
        Point3D p3 = new Point3D(-1, -1, -1);
        Point3D p4 = new Point3D(1, 1, 1);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Square distance from a point to itself
        assertEquals(Math.sqrt(0.0), p3.distance(p3));

        // TC02: Distance between a point that is in the positive quadrant of X, Y, Z, and a point that is in the negative quadrant of X, Y, Z
        double w = Math.sqrt(12.0);
        assertEquals(w, p3.distance(p4));

        // TC03: Distance between a point that is in the negative quarter of X, Y, Z, and a point that is in the positive quarter of X, Y, Z
        double x = Math.sqrt(209.0);
        assertEquals(x, p2.distance(p1));

        // TC04: Tester that the distance between point P1 and P2 equals the distance between point P2 and P1
        assertEquals(p2.distance(p1), p1.distance(p2));

        // TC05: Distance between two points, both of which are in the negative quarter of X, Y, Z
        double y = Math.sqrt(90.0);
        assertEquals(y, p2.distance(p3));

        // TC06: Distance between two points, both of which are in the positive quadrant of X, Y, Z
        double z = Math.sqrt(5.0);
        assertEquals(z, p1.distance(p4));
    }
}