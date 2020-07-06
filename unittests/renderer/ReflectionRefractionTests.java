package renderer;
import elements.*;
import geometries.Polygon;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import geometries.Sphere;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;
import scene.Scene;



class ReflectionRefractionTests {

    @Test
    public void twoSpheres() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(Color.BLACK, 0));

        scene.addGeometries(
                new Sphere(50, new Point3D(0, 0, 50), new Color(java.awt.Color.BLUE), new Material(0.4, 0.3, 100, 0.3, 0)),
                new Sphere(25, new Point3D(0, 0, 50), new Color(java.awt.Color.RED), new Material(0.5, 0.5, 100)));

        scene.addLights(new SpotLight(new Color(1000, 600, 0), new Point3D(-100, 100, -500), new Vector(-1, 1, 2), 1,
                0.0004, 0.0000006));

        ImageWriter imageWriter = new ImageWriter("twoSpheres", 150, 150, 500, 500);
        Render render = new Render(scene, imageWriter);

        render.renderImage();
        render.writeToImage();
    }


    @Test
    public void twoSpheresOnMirrors() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -10000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(10000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

        scene.addGeometries(
                new Sphere(400, new Point3D(-950, 900, 1000), new Color(0, 0, 100), new Material(0.25, 0.25, 20, 0.5, 0)),
                new Sphere(200, new Point3D(-950, 900, 1000), new Color(100, 20, 20), new Material(0.25, 0.25, 20)),
                new Triangle(new Color(20, 20, 20), new Material(0, 0, 0, 0, 1), new Point3D(1500, 1500, 1500),
                        new Point3D(-1500, -1500, 1500), new Point3D(670, -670, -3000)),
                new Triangle(new Color(20, 20, 20), new Material(0, 0, 0, 0, 0.5), new Point3D(1500, 1500, 1500),
                        new Point3D(-1500, -1500, 1500), new Point3D(-1500, 1500, 2000)));

        scene.addLights(new SpotLight(new Color(1020, 400, 400), new Point3D(-750, 750, 150),
                new Vector(-1, 1, 4), 1, 0.00001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("twoSpheresMirrored", 2500, 2500, 500, 500);
        Render render = new Render(scene, imageWriter);

        render.renderImage();
        render.writeToImage();
    }

    
    @Test
    public void trianglesTransparentSphere() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries( //
                new Triangle(Color.BLACK, new Material(0.5, 0.5, 60), //
                        new Point3D(-150, 150, 115), new Point3D(150, 150, 135), new Point3D(75, -75, 150)), //
                new Triangle(Color.BLACK, new Material(0.5, 0.5, 60), //
                        new Point3D(-150, 150, 115), new Point3D(-70, -70, 140), new Point3D(75, -75, 150)), //
                new Sphere(30, new Point3D(60, -50, 50), new Color(java.awt.Color.BLUE), new Material(0.2, 0.2, 30, 0.6, 0)));

        scene.addLights(new SpotLight(new Color(700, 400, 400), //
                new Point3D(60, -50, 0), new Vector(0, 0, 1), 1, 4E-5, 2E-7));

        ImageWriter imageWriter = new ImageWriter("shadow with transparency", 200, 200, 600, 600);
        Render render = new Render(scene, imageWriter);

        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void our_first_image1() {
        //camera center
        Scene scene = new Scene("our_masterpiece");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));

        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                //וורוד
                new Triangle(new Color(128, 49, 106), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(109.12, -63, -200),
                        new Point3D(109.12, 63, -200)),

                //  אמצע ימין
                new Triangle(new Color(52, 38, 97), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(0, 126, -200),
                        new Point3D(109.12, 63, -200)),

                // אמצע שמאל
                new Triangle(new Color(26, 98, 157), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(0, 126, -200),
                        new Point3D(-109.12, 63, -200)),

                //ימין
                new Triangle(new Color(27, 155, 192), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(-109.12, -63, -200),
                        new Point3D(-109.12, 63, -200)),

                new Sphere(192, new Point3D(0, 0, -200), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(148, new Point3D(0, 0, -200), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Triangle(Color.BLACK, new Material(0.8, 1, 10000, 0, 1),
                        new Point3D(500, 200, -100), new Point3D(-500, 200, -100), new Point3D(1800, 200, -700)),

                new Triangle(Color.BLACK, new Material(0.8, 1, 10000, 0, 1),
                        new Point3D(-500, 200, -100), new Point3D(1800, 200, -700), new Point3D(-1800, 200, -700))
        );

        scene.addLights(new DirectionalLight(new Color(10, 10, 10), new Vector(1, -1, 0)),

                new SpotLight(new Color(400, 400, 1020), new Point3D(-300, -300, -100), new Vector(2, 2, -3), 1, 0.00001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("LevLogo_cameraCenter_step7", 1000, 1000, 1000, 1000);

        Render render = new Render(scene, imageWriter);

        render.renderImage();
        imageWriter.writeToImage();
    }


    @Test
    public void our_first_image2() {
        //camera right
        Scene scene = new Scene("LevLogo_cameraRight_step7");
        Point3D camera_position = new Point3D(500, 0, -1000);
        Point3D center_of_logo = new Point3D(0, 0, -200);
        Vector camera_vector = (center_of_logo.subtract(camera_position)).normalize();
        scene.setCamera(new Camera(camera_position,camera_vector, new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                //וורוד
                new Triangle(new Color(128, 49, 106), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(109.12, -63, -200),
                        new Point3D(109.12, 63, -200)),

                //  אמצע ימין
                new Triangle(new Color(52, 38, 97), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(0, 126, -200),
                        new Point3D(109.12, 63, -200)),

                // אמצע שמאל
                new Triangle(new Color(26, 98, 157), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(0, 126, -200),
                        new Point3D(-109.12, 63, -200)),

                //ימין
                new Triangle(new Color(27, 155, 192), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(-109.12, -63, -200),
                        new Point3D(-109.12, 63, -200)),

                new Sphere(192, new Point3D(0, 0, -200), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(148, new Point3D(0, 0, -200), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Triangle(Color.BLACK, new Material(0.8, 1, 10000, 0, 1),
                        new Point3D(500, 200, -100), new Point3D(-500, 200, -100), new Point3D(1800, 200, -700)),

                new Triangle(Color.BLACK, new Material(0.8, 1, 10000, 0, 1),
                        new Point3D(-500, 200, -100), new Point3D(1800, 200, -700), new Point3D(-1800, 200, -700))
        );

        scene.addLights(new DirectionalLight(new Color(10, 10, 10), new Vector(1, -1, 0)),

                new SpotLight(new Color(400, 400, 1020), new Point3D(-300, -300, -100), new Vector(2, 2, -3), 1, 0.00001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("LevLogo_cameraRight_step7", 1000, 1000, 1000, 1000);

        Render render = new Render(scene, imageWriter);

        render.renderImage();
        imageWriter.writeToImage();
    }


    @Test
    public void our_first_image3() {
        //camera left
        Scene scene = new Scene("LevLogo_cameraLeft_step7");
        Point3D camera_position = new Point3D(-400, 0, -1000);
        Point3D center_of_logo = new Point3D(0, 0, -200);
        Vector camera_vector = (center_of_logo.subtract(camera_position)).normalize();
        scene.setCamera(new Camera(camera_position,camera_vector, new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                //וורוד
                new Triangle(new Color(128, 49, 106), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(109.12, -63, -200),
                        new Point3D(109.12, 63, -200)),

                //  אמצע ימין
                new Triangle(new Color(52, 38, 97), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(0, 126, -200),
                        new Point3D(109.12, 63, -200)),

                // אמצע שמאל
                new Triangle(new Color(26, 98, 157), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(0, 126, -200),
                        new Point3D(-109.12, 63, -200)),

                //ימין
                new Triangle(new Color(27, 155, 192), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(-109.12, -63, -200),
                        new Point3D(-109.12, 63, -200)),

                new Sphere(192, new Point3D(0, 0, -200), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(148, new Point3D(0, 0, -200), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Triangle(Color.BLACK, new Material(0.8, 1, 10000, 0, 1),
                        new Point3D(500, 200, -100), new Point3D(-500, 200, -100), new Point3D(1800, 200, -700)),

                new Triangle(Color.BLACK, new Material(0.8, 1, 10000, 0, 1),
                        new Point3D(-500, 200, -100), new Point3D(1800, 200, -700), new Point3D(-1800, 200, -700))
        );

        scene.addLights(new DirectionalLight(new Color(10, 10, 10), new Vector(1, -1, 0)),

                new SpotLight(new Color(400, 400, 1020), new Point3D(-300, -300, -100), new Vector(2, 2, -3), 1, 0.00001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("LevLogo_cameraLeft_step7", 1000, 1000, 1000, 1000);

        Render render = new Render(scene, imageWriter);

        render.renderImage();
        imageWriter.writeToImage();
    }


    @Test
    public void mini_project1_image() {
        Scene scene = new Scene("our_masterpiece2");
        Point3D camera_position = new Point3D(500, 0, -2000);
        Point3D center_of_logo = new Point3D(0, 0, -200);
        Vector camera_vector = (center_of_logo.subtract(camera_position)).normalize();

        scene.setCamera(new Camera(camera_position,camera_vector, new Vector(0, -1, 0)));
        //scene.setCamera(new Camera(new Point3D(0, 0, -3000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(2000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                //וורוד
                new Triangle(new Color(128, 49, 106), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(109.12, -63, -200),
                        new Point3D(109.12, 63, -200)),
                new Triangle(new Color(128, 49, 106), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -130),
                        new Point3D(109.12, -63, -130),
                        new Point3D(109.12, 63, -130)),
                new Polygon(new Color(128, 49, 106), new Material(1, 1, 50000, 1, 0),
                        new Point3D(109.12, -63, -200),new Point3D(109.12, -63, -130), new Point3D(0, 0, -130), new Point3D(0, 0, -200)),
                new Polygon(new Color(128, 49, 106), new Material(1, 1, 50000, 1, 0),
                        new Point3D(109.12, -63, -200),new Point3D(109.12, -63, -130), new Point3D(109.12, 63, -130), new Point3D(109.12, 63, -200)),


                //  אמצע ימין
                new Triangle(new Color(52, 38, 97), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(0, 126, -200),
                        new Point3D(109.12, 63, -200)),
                new Triangle(new Color(52, 38, 97), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -130),
                        new Point3D(0, 126, -130),
                        new Point3D(109.12, 63, -130)),
                new Polygon(new Color(52, 38, 97), new Material(1, 1, 50000, 1, 0),
                        new Point3D(0, 126, -200),new Point3D(0, 126, -130), new Point3D(109.12, 63, -130), new Point3D(109.12, 63, -200)),


                // אמצע שמאל
                new Triangle(new Color(26, 98, 157), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(0, 126, -200),
                        new Point3D(-109.12, 63, -200)),
                new Triangle(new Color(26, 98, 157), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -130),
                        new Point3D(0, 126, -130),
                        new Point3D(-109.12, 63, -130)),
                new Polygon(new Color(26, 98, 157), new Material(1, 1, 50000, 1, 0),
                        new Point3D(0, 126, -200),new Point3D(0, 126, -130), new Point3D(-109.12, 63, -130), new Point3D(-109.12, 63, -200)),


                //ימין
                new Triangle(new Color(27, 155, 192), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -200),
                        new Point3D(-109.12, -63, -200),
                        new Point3D(-109.12, 63, -200)),
                new Triangle(new Color(27, 155, 192), new Material(1, 1, 50000, 0.4, 0), new Point3D(0, 0, -130),
                        new Point3D(-109.12, -63, -130),
                        new Point3D(-109.12, 63, -130)),
                new Polygon(new Color(27, 155, 192), new Material(1, 1, 50000, 1, 0),
                        new Point3D(-109.12, -63, -200),new Point3D(-109.12, -63, -130), new Point3D(0, 0, -130), new Point3D(0, 0, -200)),
                new Polygon(new Color(27, 155, 192), new Material(1, 1, 50000, 1, 0),
                        new Point3D(-109.12, -63, -200),new Point3D(-109.12, -63, -130), new Point3D(-109.12, 63, -130), new Point3D(-109.12, 63, -200)),

                new Sphere(15, new Point3D(0, 45, -300), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(52.25, 45, -275), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(104.5, 45, -250), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(156.75, 45, -225), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(209, 45, -200), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(7.5, new Point3D(26.125, 60, -333.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(78.125, 30, -307.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(130.625, 60, -282.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(182.875, 30, -260.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(15, new Point3D(0, 45, -300), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(-52.25, 45, -275), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(-104.5, 45, -250), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(-156.75, 45, -225), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(-209, 45, -200), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(7.5, new Point3D(-26.125, 30, -333.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(-78.125, 60, -307.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(-130.625, 30, -282.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(-182.875, 60, -260.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(15, new Point3D(0, 45, -100), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(52.25, 45, -125), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(104.5, 45, -150), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(156.75, 45, -175), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(7.5, new Point3D(26.125, 30, -67.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(78.125, 60, -92.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(130.625, 30, -117.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(182.875, 60, -142.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(15, new Point3D(0, 45, -100), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(-52.25, 45, -125), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(-104.5, 45, -150), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(15, new Point3D(-156.75, 45, -175), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),

                new Sphere(7.5, new Point3D(-26.125, 60, -67.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(-78.125, 30, -92.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(-130.625, 60, -117.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),
                new Sphere(7.5, new Point3D(-182.875, 30, -142.5), Color.BLACK, new Material(0.8, 0.8, 30, 0.8, 0)),




                new Triangle(Color.BLACK, new Material(0.8, 1, 10000, 0, 2.5),
                        new Point3D(500, 200, 0), new Point3D(-500, 200, 0), new Point3D(1800, 200, -1500)),

                new Triangle(Color.BLACK, new Material(0.8, 1, 10000, 0, 1),
                        new Point3D(-500, 200, 0), new Point3D(1800, 200, -1500), new Point3D(-1800, 200, -1500)),
                new Triangle(Color.BLACK, new Material(0.8, 1, 10000, 0, 1),
                        new Point3D(-500, 200, -300), new Point3D(-500,-1400,-300), new Point3D(-1800, 200, -1500))
        );

        scene.addLights(new DirectionalLight(new Color(10, 10, 10), new Vector(1, -1, 0)),
                new SpotLight(new Color(500, 400, 400), new Point3D(0, -300, -100), new Vector(-2, 2, -3), 1, 0.00001, 0.000005),
                new SpotLight(new Color(650, 400, 300), new Point3D(-300, -300, 100) , new Vector(2, 2, -3),1.0 , 0.00001, 0.000005),
                new SpotLight(new Color(400, 400, 1020), new Point3D(-400, -300, -120), new Vector(2, 2, -3), 1, 0.00001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("LevLogo", 1000, 1000, 1000, 1000);

        Render render = new Render(scene, imageWriter,true,true);
        render.setMultithreading(4); //set num of threads
        render.renderImage();
        imageWriter.writeToImage();
    }


}