
package renderer;

import elements.*;
import primitives.*;
import geometries.Intersectable.GeoPoint;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
/**
 * Class Render represents the renderer of the system. The renderer is the mediator and the image writer. It conveys the scene that that is to be created to the image writer.
 * @author Aviya and Sima
 */
public class Render {
    private final Scene _scene;
    private ImageWriter _imageWriter;
    private boolean superSampling;
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 50;
    private static final double MIN_CALC_COLOR_K = 0.0000001;
    private static final int NUM_OF_SAMPLE_RAYS = 8; //64 rays
    private static final int SPARE_THREADS = 2;
    private boolean adaptiveSuperSampling;
    private static final int LEVEL_OF_ADAPTIVE = NUM_OF_SAMPLE_RAYS/2;

    private int _threads = 1;
    private boolean _print = false;
    /**
     * Constructor for render
     * @param _scene scene to render
     */
    public Render(Scene _scene) {
        this._scene = _scene;
        superSampling=false;
        adaptiveSuperSampling=false;
    }

    /**
     * constructor for render
     * @param scene scene to render
     * @param imageWriter image writer
     */
    public Render(Scene scene, ImageWriter imageWriter) {
        this._imageWriter = imageWriter;
        this._scene = scene;
        superSampling=false;
        adaptiveSuperSampling=false;
    }

    /**
     * Constructor for Render that also receives from user wethere he would like to use improvements in the picture
     * @param scene scene to render
     * @param imageWriter image writer
     * @param _superSampling is super sampling requested
     * @param _adaptiveSuperSampling is adaptive super sampling requested
     */
    public Render(Scene scene, ImageWriter imageWriter,boolean _superSampling,boolean _adaptiveSuperSampling){
        this._imageWriter = imageWriter;
        this._scene = scene;
        superSampling=_superSampling;
        adaptiveSuperSampling=_adaptiveSuperSampling;
    }

    /**
     * Set multithreading
     * @param threads number of threads
     * @return the Render object itself
     */
    public Render setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
        if (threads != 0)
            _threads = threads;
        else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            _threads = cores <= 2 ? 1 : cores;
        }
        return this;
    }


    /**
     * @param l vector from light source to geometry
     * @param n normal of geometry
     * @param gp GeoPoint
     * @param lightSource light source
     * @return true if point on geometry is not shaded by other objects, otherwise false
     */
    private boolean unshaded(Vector l, Vector n, GeoPoint gp, LightSource lightSource){
        Vector lightDirection = l.scale(-1); // from point to light source

        Vector delta = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : -DELTA);
        Point3D point = gp.getPoint().add(delta);
        Ray lightRay = new Ray(point, lightDirection);

        List<GeoPoint> intersections = _scene.getGeometries().findIntsersections(lightRay);
        if (intersections == null) {
            return true;
        }
        double lightDistance = lightSource.getDistance(gp.getPoint());
        for (GeoPoint gp2 : intersections) {
            if (alignZero(gp2.getPoint().distance(gp.getPoint()) - lightDistance) <= 0)
                return false;
        }
        return true;
    }

    /**
     * @param light light source
     * @param l vector from light source to geometry
     * @param n normal of geometry
     * @param geopoint GeoPoint
     * @return level of transparency of object
     */
    private double transparency(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.getPoint(), lightDirection, n);
        Point3D pointGeo = geopoint.getPoint();

        List<GeoPoint> intersections = _scene.getGeometries().findIntsersections(lightRay);
        if (intersections == null) {
            return 1d;
        }
        double lightDistance = light.getDistance(pointGeo);
        double ktr = 1d;
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.getPoint().distance(pointGeo) - lightDistance) <= 0) {
                ktr *= gp.getGeometry().getMaterial().getKT();
                if (ktr < MIN_CALC_COLOR_K) {
                    return 0.0;
                }
            }
        }
        return ktr;
    }

    /**
     * @return scene
     */
    public Scene get_scene() {
        return _scene;
    }

    /**
     * Filling the buffer according to the geometries that are in the scene.
     * This function does not create the picture file, but creates the buffer of pixels
     * If supersamling is needed it is odne here as well
     */
    public void renderImage() {
        java.awt.Color background = _scene.getBackground().getColor();
        Camera camera = _scene.getCamera();
        double distance = _scene.getDistance();

        //width and height are the width and height of the image.
        int width = (int) _imageWriter.getWidth();
        int height = (int) _imageWriter.getHeight();

        //Nx and Ny are the number of pixels in the rows
        //and columns of the view plane
        int Nx = _imageWriter.getNx(); //columns
        int Ny = _imageWriter.getNy(); //rows

        final Pixel thePixel = new Pixel(Ny, Nx);

        Thread[] threads = new Thread[_threads];
        for (int i = _threads - 1; i >= 0; --i) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();

                while (thePixel.nextPixel(pixel)) {
                    if (superSampling == false) {//         without super sampling
                        Ray ray = camera.constructRayThroughPixel(Nx, Ny, pixel.col, pixel.row, distance, width, height);
                        GeoPoint closestPoint = findClosestIntersection(ray);
                        if (closestPoint != null) {
                            java.awt.Color pixelColor = calcColor(closestPoint, ray).getColor();
                            _imageWriter.writePixel(pixel.col, pixel.row, pixelColor);
                        }
                        else{
                            _imageWriter.writePixel(pixel.col, pixel.row, background);
                        }
                    } else {//super sampling
                        if (adaptiveSuperSampling == true){//         super sampling and adaptive
                            Color result = adaptiveSuperSampling(camera, distance, Nx, Ny, width, height, pixel.col, pixel.row);
                            _imageWriter.writePixel(pixel.col, pixel.row, result.getColor());
                        }
                        else {//        super sampling without adaptive
                            List<Ray> rays = camera.constructRaysThroughPixel(Nx, Ny, pixel.col, pixel.row, distance, width, height, NUM_OF_SAMPLE_RAYS);
                            Color pixelColor = calcColor(rays);
                            _imageWriter.writePixel(pixel.col, pixel.row, pixelColor.getColor());
                        }
                    }

                }
            });
        }
        // Start threads
        for (Thread thread : threads) thread.start();
        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (Exception e) {
            }
        }
        if (_print) {
            printMessage("100%\n");
        }
    }

    /**
     * Finding the closest point to the P0 of the camera.
     *
     * @param intersectionPoints list of points, the function should find from
     *                           this list the closet point to P0 of the camera in the scene.
     * @return the closest point to the camera
     */

    private GeoPoint getClosestPoint(List<GeoPoint> intersectionPoints) {
        GeoPoint result = null;
        double mindist = Double.MAX_VALUE;

        Point3D p0 = this._scene.getCamera().get_p0();

        for (GeoPoint geo : intersectionPoints) {
            Point3D pt = geo.getPoint();
            double distance = p0.distance(pt);
            if (distance < mindist) {
                mindist = distance;
                result = geo;
            }
        }
        return result;
    }


    /**
     * Printing the grid with a fixed interval between lines
     *
     * @param interval The interval between the lines.
     */
    public void printGrid(int interval, java.awt.Color colorsep) {
        double rows = this._imageWriter.getNy();
        double collumns = _imageWriter.getNx();
        //Writing the lines.
        for (int row = 0; row < rows; ++row) {
            for (int collumn = 0; collumn < collumns; ++collumn) {
                if (collumn % interval == 0 || row % interval == 0) {
                    _imageWriter.writePixel(collumn, row, colorsep);
                }
            }
        }
    }

    /**
     * use the image writer to create an image from scene
     */
    public void writeToImage() {
        _imageWriter.writeToImage();
    }

    /**
     * calculate the avarage color of the rays in the list
     * @param superSamplingRays
     * @return color
     */
    private Color calcColor(List<Ray> superSamplingRays){
        Color color = Color.BLACK;
        for(Ray ray: superSamplingRays){
            GeoPoint intersectionPoint = findClosestIntersection(ray);
            if (intersectionPoint == null){
                color = color.add(_scene.getBackground());
            }
            else{
                color = color.add(calcColor(intersectionPoint, ray));
            }
        }
        color = color.reduce(superSamplingRays.size());
        return color;
    }
    /**
     * recursive fucntion to calculate color in certain point
     * @param coloredPoint GeoPoint- the point on the geometry where color is calculated
     * @param inRay ray
     * @param level counts how many times recursion has occured
     * @param k
     * @return color of point
     */
    private Color calcColor(GeoPoint coloredPoint, Ray inRay, int level, double k) {
        List<LightSource> lightSources = _scene.getLightSources();
        Color result = coloredPoint.getGeometry().getEmissionLight();

        Vector v = coloredPoint.getPoint().subtract(_scene.getCamera().get_p0()).normalize();
        Vector n = coloredPoint.getGeometry().getNormal(coloredPoint.getPoint());

        Material material = coloredPoint.getGeometry().getMaterial();
        int nShininess = material.getnShininess();
        double kd = material.getKd();
        double ks = material.getKs();

        if (lightSources != null) {
            for (LightSource lightSource : lightSources) {
                Vector l = lightSource.getL(coloredPoint.getPoint());
                double nl = alignZero(n.dotProduct(l));
                double nv = alignZero(n.dotProduct(v));

                if (nl * nv > 0) {
                    double ktr = transparency(lightSource, l, n, coloredPoint);

                    if (ktr * k > MIN_CALC_COLOR_K) {
                        Color ip = lightSource.getIntensity(coloredPoint.getPoint()).scale(ktr);
                        result = result.add(
                                calcDiffusive(kd, nl, ip),
                                calcSpecular(ks, l, n, nl, v, nShininess, ip));
                    }
                }
            }
        }

        if (level == 1)
            return Color.BLACK;
        double kr = material.getKR(), kkr = k * kr;
        if (kkr > MIN_CALC_COLOR_K){
            Ray reflectedRay = constructReflectedRay(coloredPoint.point, inRay, n);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if (reflectedPoint != null)
                result = result.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
        }
        double kt = material.getKT(), kkt = k * kt;
        if (kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = constructRefractedRay(coloredPoint.point, inRay, n);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null)
                result = result.add(calcColor(refractedPoint, refractedRay,
                        level - 1, kkt).scale(kt));
        }
        return result;

    }

    /**
     * calls the recursive calcColor function
     * @param geoPoint GeoPoint- the point on the geometry where color is calculated
     * @param inRay ray
     * @return color of point
     */
    private Color calcColor(GeoPoint geoPoint, Ray inRay) {
        Color color = calcColor(geoPoint, inRay, MAX_CALC_COLOR_LEVEL, 1.0);
        color = color.add(_scene.getAmbientLight().getIntensity());
        return color;
    }

    /**
     * the function call to the adaptiveSuperSampling recursion function
     *@param camera camera in scene
     * @param screenDistance distance of screen from camera
     * @param nx number of pixels on X axis
     * @param ny number of pixels on Y axis
     * @param width screen width
     * @param height screen height
     * @param col the pixel position
     * @param row the pixel position
     * @return
     */
    private  Color adaptiveSuperSampling(Camera camera, double screenDistance, int nx, int ny, double width, double height, int col, int row) {
        //double halfRx = width / nx / 2;
        //double halfRy = height / ny / 2;
        int ray1Index = 7*NUM_OF_SAMPLE_RAYS+7;
        int ray2Index = 7*NUM_OF_SAMPLE_RAYS;
        int ray3Index= 0;
        int ray4Index = 7;
        List<Ray> rays = camera.constructRaysThroughPixel(nx, ny, col, row, screenDistance, width, height, NUM_OF_SAMPLE_RAYS);
        return adaptiveSuperSampling(rays,camera, screenDistance, nx, ny, width, height, col, row
                , LEVEL_OF_ADAPTIVE, ray1Index, ray2Index, ray3Index, ray4Index);
    }

    /**
     * Calculates the color of a pixel recursively using adaptive supersampling
     * @param camera camera in scene
     * @param screenDistance distance of screen from camera
     * @param nx number of pixels on X axis
     * @param ny number of pixels on Y axis
     * @param width screen width
     * @param height screen height
     *
     * @param level_of_adaptive the level of the recursion
     * @return Color
     */
    private Color adaptiveSuperSampling(List<Ray> rays, Camera camera, double screenDistance, int nx, int ny, double width, double height, int col, int row
            , int level_of_adaptive,int ray1Index, int ray2Index, int ray3Index, int ray4Index) {
        int numOfAdaptiveRays = 5;
        Color centerColor;
        Color color1 ;
        Color color2 ;
        Color color3 ;
        Color color4 ;

        Ray centerRay = rays.get(rays.size()-1);
        GeoPoint gp = findClosestIntersection(centerRay);
        if(  gp == null )
            centerColor= _scene.getBackground();
        else
            centerColor=calcColor(gp,centerRay);
        Ray ray1 = rays.get(ray1Index);
        if( findClosestIntersection(ray1) == null )
            color1= _scene.getBackground();
        else
            color1=calcColor(findClosestIntersection(ray1),ray1);
        Ray ray2 = rays.get(ray2Index);
        if( findClosestIntersection(ray2) == null )
            color2= _scene.getBackground();
        else
            color2=calcColor(findClosestIntersection(ray2), ray2);
        Ray ray3 = rays.get(ray3Index);
        if( findClosestIntersection(ray3) == null )
            color3= _scene.getBackground();
        else
            color3=calcColor(findClosestIntersection(ray3), ray3);
        Ray ray4 = rays.get(ray4Index);
        if( findClosestIntersection(ray4) == null )
            color4= _scene.getBackground();
        else
            color4=calcColor(findClosestIntersection(ray4),ray4);
        if (level_of_adaptive == 0)
        {
            centerColor = centerColor.add(color1,color2, color3,color4);
            return centerColor.reduce(numOfAdaptiveRays);
        }
        if (color1.isColorsEqual(centerColor) && color2.isColorsEqual(centerColor) && color3.isColorsEqual(centerColor) && color4.isColorsEqual(centerColor))
        {
            return centerColor;
        }
        else {
            if (!color1.isColorsEqual(centerColor)) {
                color1 = color1.add(adaptiveSuperSampling(rays, camera, screenDistance, nx, ny, width, height, col, row, level_of_adaptive - 1,ray1Index - (NUM_OF_SAMPLE_RAYS+1), ray2Index, ray3Index, ray4Index ));
                color1 = color1.reduce(2);
            }
            if (!color2.isColorsEqual(centerColor)) {;
                color2 = color2.add(adaptiveSuperSampling(rays,camera, screenDistance, nx, ny, width, height, col, row, level_of_adaptive - 1,ray1Index, ray2Index-(NUM_OF_SAMPLE_RAYS-1), ray3Index, ray4Index));
                color2 = color2.reduce(2);
            }
            if (!color3.isColorsEqual(centerColor)) {
                color3 = color3.add(adaptiveSuperSampling(rays,camera, screenDistance, nx, ny, width, height, col, row, level_of_adaptive - 1,ray1Index, ray2Index, ray3Index+(NUM_OF_SAMPLE_RAYS+1), ray4Index));
                color3 = color3.reduce(2);
            }
            if (!color4.isColorsEqual(centerColor)) {
                color4 = color4.add(adaptiveSuperSampling(rays,camera, screenDistance, nx, ny, width, height, col, row, level_of_adaptive - 1,ray1Index, ray2Index, ray3Index, ray4Index+(NUM_OF_SAMPLE_RAYS-1)));
                color4 = color4.reduce(2);
            }
            centerColor = centerColor.add(color1, color2, color3, color4);
            return centerColor.reduce(numOfAdaptiveRays);

        }
    }

    /**
     * Find intersections of a ray with the scene geometries and get the
     * intersection point that is closest to the ray head. If there are no
     * intersections, null will be returned.
     *
     * @param ray intersecting the scene
     * @return the closest point
     */
    private GeoPoint findClosestIntersection(Ray ray) {

        if (ray == null) {
            return null;
        }
        List<GeoPoint> intersections = _scene.getGeometries().findIntsersections(ray);
        if (intersections == null)
            return null;
        GeoPoint closestPoint = getClosestPoint(intersections);
        return closestPoint;
    }

    /**
     * construct a refracted ray
     * @param pointGeo GeoPoint- the point on the geometry where color is calculated
     * @param inRay ray
     * @param n
     * @return refracted ray
     */
    private Ray constructRefractedRay(Point3D pointGeo, Ray inRay, Vector n) {
        return new Ray(pointGeo, inRay.getDirection(), n);
    }

    /**
     * construct a reflected ray
     * @param pointGeo GeoPoint- the point on the geometry where color is calculated
     * @param inRay ray
     * @param n
     * @return reflected ray
     */
    private Ray constructReflectedRay(Point3D pointGeo, Ray inRay, Vector n) {
        Vector v = inRay.getDirection();
        double vn = v.dotProduct(n);

        if (vn == 0) {
            return null;
        }

        Vector r = v.subtract(n.scale(2 * vn));
        return new Ray(pointGeo, r, n);
    }


    /**
     * Calculate Specular component of light reflection.
     *
     * @param ks         specular component coef
     * @param l          direction from light to point
     * @param n          normal to surface at the point
     * @param nl         dot-product n*l
     * @param v          direction from point of view to point
     * @param nShininess shininess level
     * @param ip         light intensity at the point
     * @return specular component light effect at the point
     * Finally, the Phong model has a provision for a highlight, or specular, component, which reflects light in a
     * shiny way. This is defined by [rs,gs,bs](-V.R)^p, where R is the mirror reflection direction vector we discussed
     * in class (and also used for ray tracing), and where p is a specular power. The higher the value of p, the shinier
     * the surface.
     */
    private Color calcSpecular(double ks, Vector l, Vector n, double nl, Vector v, int nShininess, Color ip) {
        double p = nShininess;

        Vector R = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -alignZero(R.dotProduct(v));
        if (minusVR <= 0) {
            return Color.BLACK; // view from direction opposite to r vector
        }
        // [rs,gs,bs](-V.R)^p
        return ip.scale(ks * Math.pow(minusVR, p));
    }

    /**
     * Calculate Diffusive component of light reflection.
     *
     * @param kd diffusive component coef
     * @param nl dot-product n*l
     * @param ip light intensity at the point
     * @return diffusive component of light reflection
     * @author Dan Zilberstein
     * <p>
     * The diffuse component is that dot product nâ€¢L that we discussed in class. It approximates light, originally
     * from light source L, reflecting from a surface which is diffuse, or non-glossy. One example of a non-glossy
     * surface is paper. In general, you'll also want this to have a non-gray color value,
     * so this term would in general be a color defined as: [rd,gd,bd](nâ€¢L)
     */
    private Color calcDiffusive(double kd, double nl, Color ip) {
        if (nl < 0) {
            nl = -nl;
        }

        return ip.scale(nl * kd);
    }




    /**
     * @param val value
     * @return sign of value
     */
    private boolean sign(double val) {
        return (val > 0d);
    }

    /**
     * print message
     * @param msg
     */
    private synchronized void printMessage(String msg) {
        synchronized (System.out) {
            System.out.println(msg);
        }
    }
    /**
     * Pixel is an internal helper class whose objects are associated with a Render object that
     * they are generated in scope of. It is used for multithreading in the Renderer and for follow up
     * its progress.
     * There is a main follow up object and several secondary objects - one in each thread.
     */
    private class Pixel {
        public volatile int row = 0;
        public volatile int col = -1;
        private long _maxRows = 0;
        private long _maxCols = 0;
        private long _pixels = 0;
        private long _counter = 0;
        private int _percents = 0;
        private long _nextCounter = 0;

        /**
         * The constructor for initializing the main follow up Pixel object
         *
         * @param maxRows the amount of pixel rows
         * @param maxCols the amount of pixel columns
         */
        public Pixel(int maxRows, int maxCols) {
            _maxRows = maxRows;
            _maxCols = maxCols;
            _pixels = maxRows * maxCols;
            _nextCounter = _pixels / 100;
            if (Render.this._print) {
                printMessage(String.format(" %02d%%", _percents));
            }
        }


        /**
         * Default constructor for secondary Pixel objects
         */
        public Pixel() { }

        /**
         * Internal function for thread-safe manipulating of main follow up Pixel object - this function is
         * critical section for all the threads, and main Pixel object data is the shared data of this critical
         * section.
         * The function provides next pixel number each call.
         *
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return the progress percentage for follow up: if it is 0 - nothing to print, if it is -1 - the task is
         * finished, any other value - the progress percentage (only when it changes)
         */
        private synchronized int nextP(Pixel target) {
            ++col;
            ++_counter;
            if (col < _maxCols) {
                target.row = this.row;
                target.col = this.col;
                if (_print && _counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            ++row;
            if (row < _maxRows) {
                col = 0;
                if (_print && _counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            return -1;
        }

        /**
         * Public function for getting next pixel number into secondary Pixel object.
         * The function prints also progress percentage in the console window.
         *
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percents = nextP(target);
            if (_print && percents > 0) {
                printMessage(String.format(" %02d%%", _percents));
            }
            if (percents >= 0)
                return true;
            if (_print) {
                printMessage(String.format(" %02d%%", 100));
            }
            return false;
        }
    }

}
