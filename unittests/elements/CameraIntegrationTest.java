package elements;

import elements.Camera;
import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraIntegrationTest {

    Camera camera1 = new Camera(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0));
    Camera camera2 = new Camera(new Point3D(0, 0, -0.5), new Vector(0, 0, 1), new Vector(0, -1, 0));

    @Test
    void constructRayThroughPixelWithSphere() {

        // TC01: sphere r = 1, 2 intersection points
        Sphere sph0 = new Sphere(1, new Point3D(0, 0, 3));
        List<Intersectable.GeoPoint> results1;
        int count = 0;
        // The number of pixels on the axes
        int Nx = 3;
        int Ny = 3;
        // A loop that summarizes all the intersection points with the ball from all pixels
        for (int i = 0; i < Ny; ++i) {
            for (int j = 0; j < Nx; ++j) {
                Ray ray = camera1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3);
                results1 = sph0.findIntsersections(ray);
                if (results1 != null)
                    count += results1.size();
            }
        }
        assertEquals(2, count);


        //TC02: sphere r = 2.5, 18 intersection points
        Sphere sph1 =  new Sphere(2.5, new Point3D(0, 0, 2.5));
        List<Intersectable.GeoPoint> results2;
        count = 0;
        // The number of pixels on the axes
        Nx =3;
        Ny =3;
        // A loop that summarizes all the intersection points with the ball from all pixels
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results2 = sph1.findIntsersections(camera2.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results2 != null)
                    count += results2.size();
            }
        }
        assertEquals(18,count);

        //TC03: sphere r = 2, 10 intersection points
        Sphere sph3 =  new Sphere(2, new Point3D(0, 0, 2));
        List<Intersectable.GeoPoint> results3;
        count = 0;
        // The number of pixels on the axes
        Nx =3;
        Ny =3;
        // A loop that summarizes all the intersection points with the ball from all pixels
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results3 = sph3.findIntsersections(camera2.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results3 != null)
                    count += results3.size();
            }
        }
        assertEquals(10,count);


        // TC04: sphere r = 4, 9 intersection points
        Sphere sph4 =  new Sphere(4, new Point3D(0, 0, 0.5));
        List<Intersectable.GeoPoint> results4;
        count = 0;
        // The number of pixels on the axes
        Nx =3;
        Ny =3;
        // A loop that summarizes all the intersection points with the ball from all pixels
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results4 = sph4.findIntsersections(camera1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results4 != null)
                    count += results4.size();
            }
        }
        assertEquals(9,count);




        //TC05: sphere r = 0.5, 9 intersection points
        Sphere sph5 =  new Sphere(0.5, new Point3D(0, 0, -1));
        List<Intersectable.GeoPoint> results5;
        count = 0;
        // The number of pixels on the axes
        Nx =3;
        Ny =3;
        // A loop that summarizes all the intersection points with the ball from all pixels
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results5 = sph5.findIntsersections(camera1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results5 != null)
                    count += results5.size();
            }
        }
        assertEquals(0,count);

    }
    @Test
    void constructRayThroughPixelWithPlane() {

        //TC01 9 intersection points- plane against camera
        Plane p = new Plane(new Vector(0.0, 0.0, 5.0), new Point3D(0.0, 0.0, 1.0));
        List<Intersectable.GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < Ny; ++i) {
            for (int j = 0; j < Nx; ++j) {
                results = p.findIntsersections(camera1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(9, count);

        //TC02 Nine intersection points- plane at angle
        p = new Plane(new Vector(0.0, -1.0, 5.0), new Point3D(0.0, 0.0, 1.0));
        count = 0;
        for (int i = 0; i < Ny; ++i) {
            for (int j = 0; j < Nx; ++j) {
                results = p.findIntsersections(camera1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(9, count);

        //TC03 6 intersection points- plane at angle
        p = new Plane(new Vector(0.0, -10.0, 10.0), new Point3D(0.0, 0.0, 1.0));
        count = 0;
        for (int i = 0; i < Ny; ++i) {
            for (int j = 0; j < Nx; ++j) {
                results = p.findIntsersections(camera1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(6, count);
    }

    @Test
    void constructRayThroughPixelWithTriangle() {
        //TC01 1 intersection point
        Triangle t = new Triangle(new Point3D(0.0, -1.0, 2.0), new Point3D(1.0, 1.0, 2.0), new Point3D(-1.0, 1.0, 2.0));
        List<Intersectable.GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < Ny; ++i) {
            for (int j = 0; j < Nx; ++j) {
                results = t.findIntsersections(camera1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(1, count);

        //TC02 2 intersection points
        t = new Triangle(new Point3D(0.0, -20.0, 2.0), new Point3D(1.0, 1.0, 2.0), new Point3D(-1.0, 1.0, 2.0));
        count = 0;
        for (int i = 0; i < Ny; ++i) {
            for (int j = 0; j < Nx; ++j) {
                results = t.findIntsersections(camera1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(2, count);
    }
}
