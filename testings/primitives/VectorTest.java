package primitives;

import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 * @author Dan
 */

class VectorTest {
    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */

    @Test
    void dotProduct() {
        Vector v1 = new Vector(4.0, 5.0, -7.0);
        Vector v2 = new Vector(-4.0, 5.0, -7.0);
        Vector v3 = new Vector(-4.0, -5.0, -7.0);
        Vector v4 = new Vector(4.0, -5.0, -7.0);
        Vector v5 = new Vector(4.0, 0.0, -7.0);
        Vector v7 = new Vector(0.0, 5.0, -7.0);
        Vector v8 = new Vector(0.0, -5.0, -7.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: The first vector is in the positive quarter of X, Y
        // and the negative quarter of Z. The second vector is in the positive quarter of Y, and the negative quarter of X, Z
        assertEquals(58, v1.dotProduct(v2));

        // TC02: The first vector is in the positive quarter of X, Y and the negative quarter of Z.
        // The second vector is in the positive quarter of X, Y, Z
        assertEquals(8, v1.dotProduct(v3));

        // TC03: The first vector is in the positive quarter of X, Y and the negative quarter of Z.
        // The second vector is in the positive quarter of X, and the negative quarter of Y, Z
        assertEquals(40, v1.dotProduct(v4));
        assertEquals(24, v2.dotProduct(v8));

        // TC04: The first vector is in the positive quarter of Y and the negative quarter of X, Z.
        // The second vector is in the negative quarter of X, Y, Z
        assertEquals(40, v2.dotProduct(v3));

        // TC05: The first vector is in the positive quarter of Y and the negative quarter of X, Z.
        // The second vector is in the positive quarter of X and the negative quarter of Y, Z
        assertEquals(8, v2.dotProduct(v4));

        // TC06: The first vector is in the negative quarter of X, Y, Z.
        // The second vector is in the positive quarter of X and the negative quarter of Y, Z
        assertEquals(58, v3.dotProduct(v4));

        // TC07: The first vector is in the positive quarter of X, Y and the negative quarter of Z.
        //The second vector is in the positive quarter of, YX and the negative quarter of Z
        assertEquals(65, v1.dotProduct(v5));
        assertEquals(74, v1.dotProduct(v7));

        //TC08: Checks orthogonal vectors
        v1 = new Vector(1, 2, 3);
        v2 = new Vector(-2, -4, -6);
        v3 = new Vector(0, 3, -2);

        if (!isZero(v1.dotProduct(v3)))
            out.println("ERROR: dotProduct() for orthogonal vectors is not zero");
        if (!isZero(v1.dotProduct(v2) + 28))
            out.println("ERROR: dotProduct() wrong value");

    }

    @Test
    void crossProduct() {
        Vector v1 = new Vector(1.0, 1.0, 1.0);
        Vector v2 = new Vector(2.0,2.0,1.0);
        Vector v3 = new Vector(-1.0, -1.0, -2.0);
        Vector v4 = new Vector(2.0,3.0,4.0);
        Vector v5 = new Vector(5.0,6.0,7.0);
        Vector v6 = new Vector(0.0,0.0,-1.0);
        Vector v7 = new Vector(0.0,1.0,0.0);
        // ============ Equivalence Partitions Tests ==============

        // TC01: The two vector are in the positive quadrant of X, Y, Z
        assertEquals(new Vector(-1.0, 1.0, 0.0),  v1.crossProduct(v2));

        // TC02: The first vector are in the positive quadrant of X, Y, Z
        // and The second vector is in the negative quadrant of X, Y, Z
        assertEquals(new Vector(-1.0, 1.0, 0.0),  v1.crossProduct(v3));

        // TC03: the different direction
        assertEquals(v1.crossProduct(v3).scale(-1),  v3.crossProduct(v1));

        // TC04: The first vector is in the positive quadrant of X, Y, Z
        // and The second vector is in the positive quadrant of X, Y, Z
        assertEquals(new Vector(new Point3D(-3,6,-3)), v4.crossProduct(v5));

        // TC05: The first vector is on the negative Z axis
        // and The vector is on the positive Y axis
        assertEquals(new Vector(new Point3D(1,0,0)), v6.crossProduct(v7));


        v1 = new Vector(1, 2, 3);
        v2 = new Vector(-2, -4, -6);
        v3 = new Vector(0, 3, -2);

        // TC06: Checks orthogonal vectors
        try { // test zero vector
            v1.crossProduct(v2);
            out.println("ERROR: crossProduct() for parallel vectors does not throw an exception");
        } catch (Exception e) {
        }

        // TC07: Checks length of the result
        Vector vr = v1.crossProduct(v3);
        if (!isZero(vr.length() - v1.length() * v3.length()))
            out.println("ERROR: crossProduct() wrong result length");

        // TC08: Checks result orthogonal to its operands
        if (!isZero(vr.dotProduct(v1)) || !isZero(vr.dotProduct(v3)))
            out.println("ERROR: crossProduct() result is not orthogonal to its operands");

    }

    @Test
    void subtract() {
        Vector v1 = new Vector(4.0, 5.0, -7.0);
        Vector v2 = new Vector(-4.0, 5.0, -7.0);
        Vector v3 = new Vector(-4.0, -5.0, -7.0);
        Vector v4 = new Vector(4.0, -5.0, -7.0);
        Vector v5 = new Vector(4.0, 0.0, -7.0);
        Vector v6 = new Vector(-4.0, 0.0, -7.0);
        Vector v7 = new Vector(0.0, 5.0, -7.0);
        Vector v8 = new Vector(0.0, -5.0, -7.0);
        // ============ Equivalence Partitions Tests ==============

        // TC01: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of Y, and the negative quarter of X, Z
        assertEquals(new Vector(8.0,0.0,0.0), v1.subtract(v2));

        // TC02: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of X, Y, Z
        assertEquals(new Vector(8.0,10.0,0.0), v1.subtract(v3));

        // TC03: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of X, and the negative quarter of Y, Z
        assertEquals(new Vector(0.0,10.0,0.0), v1.subtract(v4));

        // TC04: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of, YX and the negative quarter of Z
        assertEquals(new Vector(0.0,5.0,0.0), v1.subtract(v5));

        // TC05: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of y and the negative quarter of x, z
        assertEquals(new Vector(8.0,5.0,0.0), v1.subtract(v6));

        // TC06: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of, YX and the negative quarter of Z
        assertEquals(new Vector(4.0,0.0,0.0), v1.subtract(v7));

        // TC07: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of X, and the negative quarter of Y, Z
        assertEquals(new Vector(4.0,10.0,0), v1.subtract(v8));

    }

    @Test
    void add() {
        Vector v1 = new Vector(4.0, 5.0, -7.0);
        Vector v2 = new Vector(-4.0, 5.0, -7.0);
        Vector v3 = new Vector(-4.0, -5.0, -7.0);
        Vector v4 = new Vector(4.0, -5.0, -7.0);
        Vector v5 = new Vector(4.0, 0.0, -7.0);
        Vector v6 = new Vector(-4.0, 0.0, -7.0);
        Vector v7 = new Vector(0.0, 5.0, -7.0);
        Vector v8 = new Vector(0.0, -5.0, -7.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of Y, and the negative quarter of X, Z
        assertEquals(new Vector(0.0,10.0,-14.0), v1.add(v2));

        // TC02: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of X, Y, Z
        assertEquals(new Vector(0.0,0.0,-14.0), v1.add(v3));

        // TC03: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of X, and the negative quarter of Y, Z
        assertEquals(new Vector(8.0,0.0,-14.0), v1.add(v4));

        // TC04: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of, YX and the negative quarter of Z
        assertEquals(new Vector(8.0,5.0,-14.0), v1.add(v5));

        // TC05: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of y and the negative quarter of x, z
        assertEquals(new Vector(0.0,5.0,-14.0), v1.add(v6));

        // TC06: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of, YX and the negative quarter of Z
        assertEquals(new Vector(4.0,10.0,-14.0), v1.add(v7));

        // TC07: The first vector is in the positive quarter of X, Y and the negative quarter of Z
        //The second vector is in the positive quarter of X, and the negative quarter of Y, Z
        assertEquals(new Vector(4.0,0.0,-14.0), v1.add(v8));
    }

    @Test
    void lengthSquared() {
        Vector v1 = new Vector(3.0, 2.0, 1.0);
        Vector v2 = new Vector(-5.0,-6.0,-8.0);
        Vector v3 = new Vector(-1.0, 1.0, 1.0);
        Vector v4 = new Vector(1.0, -1.0, 1.0);
        Vector v5 = new Vector(2.0, 3.0, -1.0);
        Vector v6 = new Vector(1.0, -1.0, -1.0);
        Vector v7 = new Vector(-6.0, -1.0, 2.0);
        Vector v8 = new Vector(-5.0, 8.0, -5.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: The vector is in the positive quarter of X, Y ,Z
        assertEquals(14.0, v1.lengthSquared());

        // TC02: The vector is in the negative quarter of X, Y ,Z
        assertEquals(125.0, v2.lengthSquared());

        // TC03: The vector is in the positive quarter of  Y ,Z and negative quarter of X
        assertEquals(3.0, v3.lengthSquared());

        // TC04: The first vector is in the positive quarter of X ,Z and negative quarter of Y
        assertEquals(3.0, v4.lengthSquared());

        // TC05: The vector is in the positive quarter of  X, Y and negative quarter of Z
        assertEquals(14.0, v5.lengthSquared());

        // TC06: The vector is in the positive quarter of  X and negative quarter of Y, Z
        assertEquals(3.0, v6.lengthSquared());

        // TC07: The vector is in the positive quarter of  Z and negative quarter of X, Y
        assertEquals(41.0, v7.lengthSquared());

        // TC08: The vector is in the positive quarter of  Y and negative quarter of X, Z
        assertEquals(114.0, v8.lengthSquared());


    }

    @Test
    void length() {
        Vector v1 = new Vector(3.0, 2.0, 1.0);
        Vector v2 = new Vector(-5.0,-6.0,-8.0);
        Vector v3 = new Vector(-1.0, 1.0, 1.0);
        Vector v4 = new Vector(1.0, -1.0, 1.0);
        Vector v5 = new Vector(2.0, 3.0, -1.0);
        Vector v6 = new Vector(1.0, -1.0, -1.0);
        Vector v7 = new Vector(-6.0, -1.0, 2.0);
        Vector v8 = new Vector(-5.0, 8.0, -5.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: The vector is in the positive quarter of X, Y ,Z
        double x = Math.sqrt(14.0);
        assertEquals(x, v1.length());

        // TC02: The vector is in the negative quarter of X, Y ,Z
        x = Math.sqrt(125.0);
        assertEquals(x, v2.length());

        // TC03: The vector is in the positive quarter of  Y ,Z and negative quarter of X
        x = Math.sqrt(3.0);
        assertEquals(x, v3.length());

        // TC04: The first vector is in the positive quarter of X ,Z and negative quarter of Y
        x = Math.sqrt(3.0);
        assertEquals(x, v4.length());

        // TC05: The vector is in the positive quarter of  X, Y and negative quarter of Z
        x = Math.sqrt(14.0);
        assertEquals(x, v5.length());

        // TC06: The vector is in the positive quarter of  X and negative quarter of Y, Z
        x = Math.sqrt(3.0);
        assertEquals(x, v6.length());

        // TC07: The vector is in the positive quarter of  Z and negative quarter of X, Y
        x = Math.sqrt(41.0);
        assertEquals(x, v7.length());

        // TC08: The vector is in the positive quarter of  Y and negative quarter of X, Z
        x = Math.sqrt(114.0);
        assertEquals(x, v8.length());

    }

    @Test
    void normalize() {
        Vector v1 = new Vector(3.0, 2.0, 1.0);
        Vector v2 = new Vector(-1.0, 1.0, 1.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Normalize a vector that is in the positive quadrant of X, Y, Z
        v1.normalize();
        assertEquals(1, v1.length());

        // TC01: Normalize a vector that is in the positive quadrant of Y, Z and the negative quadrant of X
        v2.normalize();
        assertEquals(1, v2.length());

    }

    @Test
    void normalized() {
        Vector v1 = new Vector(3.0, 2.0, 1.0);
        Vector v2 = new Vector(-1.0, 1.0, 1.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Normalized a vector that is in the positive quadrant of X, Y, Z
        Vector result = new Vector(v1.normalized());
        assertEquals(1, result.length());

        // TC01: Normalized a vector that is in the positive quadrant of Y, Z and the negative quadrant of X
        result = v2.normalized();
        assertEquals(1, result.length());
    }

    @Test
    void scale() {
        Vector v1 = new Vector(4.0, 5.0, -7.0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Duplication in positive scalar
        assertEquals(new Vector(-4.0, -5.0, 7.0), v1.scale(-1));

        // TC01: Duplication in negative scalar
        assertEquals(new Vector(12, 15, -21), v1.scale(3));
    }

}
