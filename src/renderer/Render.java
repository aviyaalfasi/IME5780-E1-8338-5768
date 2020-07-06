
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
    private boolean superSampling;//is super sampling wanted?
    private static final double DELTA = 0.1;//small number used for scaling shadow rays
    private static final int MAX_CALC_COLOR_LEVEL = 50; //the maximum recursion level for calcColor
    private static final double MIN_CALC_COLOR_K = 0.0000001;//minimum transparency level for transparency rays
    private static final int NUM_OF_SAMPLE_RAYS = 9; //81 rays throw pixel for super sampling
    private static final int SPARE_THREADS = 2;//number of spare threads is 2 because maximum of threads to be used in paralel is 3
    private boolean adaptiveSuperSampling;//is adaptive super sampling wanted?
    private static final int LEVEL_OF_ADAPTIVE = NUM_OF_SAMPLE_RAYS/2;//number of times to repeat recursion of adaptive super samling

    private int _threads = 1;//number of threads to use when running program- default is one
    private boolean _print = true;//print completion of thread?
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
     * Set multithreading- number of threads to use
     * @param threads number of threads
     * @return the Render object itself
     */
    public Render setMultithreading(int threads) {
        if (threads < 0)//threads cannot be less than zero
            throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
        if (threads != 0)//any number that is not zero is acceptable
            _threads = threads;
        else {//if number received was zero:
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            _threads = cores <= 2 ? 1 : cores;//if cores is smaller than 2 than threads is 1, i=otherwise it is equal to cores
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
        Vector lightDirection = l.scale(-1); //vector from point to light source

        Vector delta = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : -DELTA);//if the dot product between light direction and the normal off the geometry is bigger than zero, scale the normal by delta (a very small number), otherwise scale by -delta
        Point3D point = gp.getPoint().add(delta);//move point that color will be calculated in a bit closer to light
        Ray lightRay = new Ray(point, lightDirection);//create a shadow ray from the point to the light source

        List<GeoPoint> intersections = _scene.getGeometries().findIntsersections(lightRay);//find the intersection of the shadow ray with geometries in scene that may be blocking the light
        if (intersections == null) {//there are no intersection points
            return true;
        }
        double lightDistance = lightSource.getDistance(gp.getPoint());//get distance of light source from geometry
        for (GeoPoint gp2 : intersections) {
            if (alignZero(gp2.getPoint().distance(gp.getPoint()) - lightDistance) <= 0)
                return false;//check every geometry that shadow ray intersected. if their distance from the geometry is smaller than the distance of the light source from the geometry then they are blocking the light, and the geometry is shaded.
        }
        return true;//if all geometries were behing light, the geometry is unshaded.
    }

    /**
     * @param light light source
     * @param l vector from light source to geometry
     * @param n normal of geometry
     * @param geopoint GeoPoint
     * @return level of transparency of object
     */
    private double transparency(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
        double transparency_level=1;
        double un_transparency_level=0;
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.getPoint(), lightDirection, n);//ray in opposite direction of vector L from light, scaled by delta
        Point3D pointGeo = geopoint.getPoint();

        List<GeoPoint> intersections = _scene.getGeometries().findIntsersections(lightRay);//geometries that might be in between light source and geometry
        if (intersections == null) {
            return transparency_level;//if there are no geometries that intersect with the ray then it is transparent
        }
        double lightDistance = light.getDistance(pointGeo);//distance of light source from geometry
        double ktr = transparency_level;//transparency level
        for (GeoPoint gp : intersections) {
            //for each geometry that was intersected by ray, check whethter it is
            //located in front or behind the light: if the distance from the geometry to
            // the point is smaller than the distance of the light source to the point
            // then it is in front of the light.
            if (alignZero(gp.getPoint().distance(pointGeo) - lightDistance) <= 0) {
                //if the geometry was in front of the light multiply ktr by its transparency level
                ktr *= gp.getGeometry().getMaterial().getKT();
                if (ktr < MIN_CALC_COLOR_K) {
                    //if it is smaller than MIN_CALC_COLOR_K then consider it opaque,
                    // no light goes through it and its transparency level is zero
                    return un_transparency_level;
                }
            }
        }
        return ktr;//return ktr
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

        //create thread foreach ray calculation
        Thread[] threads = new Thread[_threads];
        for (int i = _threads - 1; i >= 0; --i) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();

                while (thePixel.nextPixel(pixel)) {
                    if (superSampling == false) {//         without super sampling
                        //create ray throw this pixel
                        Ray ray = camera.constructRayThroughPixel(Nx, Ny, pixel.col, pixel.row, distance, width, height);
                        Color pixelColor = calcColor(ray);  //calculate the color for the pixel
                        _imageWriter.writePixel(pixel.col, pixel.row, pixelColor.getColor());  //write the color to the pixel
                    } else {//super sampling
                        if (adaptiveSuperSampling == true){//         super sampling and adaptive
                            //calculate the color for the pixel using adaptive improve
                            Color result = adaptiveSuperSampling(camera, distance, Nx, Ny, width, height, pixel.col, pixel.row);
                            _imageWriter.writePixel(pixel.col, pixel.row, result.getColor()); //write the color to the pixel
                        }
                        else {//        super sampling without adaptive
                            //calculate the color for the pixel using super sampling improve
                            Color pixelColor = superSampling(camera, distance, width, height, Nx, Ny, pixel);
                            _imageWriter.writePixel(pixel.col, pixel.row, pixelColor.getColor()); //write the color to the pixel
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
     * This function returns the color in pixels using the enhanced calculation super Sampling
     * @param camera the scene camera
     * @param distance the distance from the camera to the center of the screen
     * @param width width of the screen
     * @param height height of the screen
     * @param nx number of pixel on the x axis
     * @param ny number of pixel on the y axis
     * @param pixel the pixel to calculate its color
     * @return Color
     */
    private Color superSampling(Camera camera, double distance, int width, int height, int nx, int ny, Pixel pixel) {
        List<Ray> rays = camera.constructRaysThroughPixel(nx, ny, pixel.col, pixel.row, distance, width, height, NUM_OF_SAMPLE_RAYS);  //calculate rays throw this pixel
        return calcColor(rays);  //calculate the color of the pixel
    }

    /**
     * Finding the closest point to the P0 of the camera.
     * @param intersectionPoints list of points, the function should find from
     *                           this list the closet point to P0 of the camera in the scene.
     * @return the closest point to the camera
     */

    private GeoPoint getClosestPoint(List<GeoPoint> intersectionPoints) {
        GeoPoint result = null;
        double mindist = Double.MAX_VALUE; //max number

        Point3D p0 = this._scene.getCamera().get_p0();

        for (GeoPoint geo : intersectionPoints) { //foreach intersection point
            Point3D pt = geo.getPoint();
            double distance = p0.distance(pt); //the distance between the point and the camera
            if (distance < mindist) {  //set the new minimum distance
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
     * @param superSamplingRays list of rays throw pixel
     * @return color
     */
    private Color calcColor(List<Ray> superSamplingRays){
        Color color = Color.BLACK;
        for(Ray ray: superSamplingRays){    //Calculate the color for each of the rays
            color = color.add(calcColor(ray));  //sum up the color scheme
        }
        color = color.reduce(superSamplingRays.size());   //Reduces the result color by the number of rays in the list (average calculation)
        return color;
    }
    /**
     * recursive fucntion to calculate color in certain point
     * @param coloredPoint GeoPoint- the point on the geometry where color is calculated
     * @param inRay ray
     * @param level counts how many times recursion has occured
     * @param k parameter to calculate the material
     * @return color of point
     */
    private Color calcColor(GeoPoint coloredPoint, Ray inRay, int level, double k) {
        List<LightSource> lightSources = _scene.getLightSources(); //the light sources of the scene
        Color result = coloredPoint.getGeometry().getEmissionLight(); //emission light of geometry

        Vector v = coloredPoint.getPoint().subtract(_scene.getCamera().get_p0()).normalize();//vector from camera to geometry
        Vector n = coloredPoint.getGeometry().getNormal(coloredPoint.getPoint());//normal to the point on the geometry

        Material material = coloredPoint.getGeometry().getMaterial();
        int nShininess = material.getnShininess();
        double kd = material.getKd();
        double ks = material.getKs();

        if (lightSources != null) {
            for (LightSource lightSource : lightSources) { //for each light source calculate the color in the point
                Vector l = lightSource.getL(coloredPoint.getPoint());//vector from light source to point on geometry
                double nl = alignZero(n.dotProduct(l));//dot product of normal off geometry and l
                double nv = alignZero(n.dotProduct(v));//dot product of normal off geometry and vector from camera to geometry

                if (nl * nv > 0) {
                    double ktr = transparency(lightSource, l, n, coloredPoint);//get transparency level of geometry

                    if (ktr * k > MIN_CALC_COLOR_K) {//if the transparency level is not below saved minimum
                        //calculation of color according to phong reflectance model (with diffusive and specular...)
                        Color ip = lightSource.getIntensity(coloredPoint.getPoint()).scale(ktr);
                        result = result.add(
                                calcDiffusive(kd, nl, ip), //add the diffuse
                                calcSpecular(ks, l, n, nl, v, nShininess, ip));  //add the specular
                    }
                }
            }
        }

        if (level == 1)//end of recursion
            return Color.BLACK;
        double kr = material.getKR(), kkr = k * kr;
        if (kkr > MIN_CALC_COLOR_K){//if the reflectance level is not below saved minimum
            Ray reflectedRay = constructReflectedRay(coloredPoint.point, inRay, n);//reflectance ray
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);//closest intersection point of reflection ray with geometries to the geometry
            if (reflectedPoint != null)
                result = result.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));//recursion
        }
        double kt = material.getKT(), kkt = k * kt;
        if (kkt > MIN_CALC_COLOR_K) {//if the transparency level is not below saved minimum
            Ray refractedRay = constructRefractedRay(coloredPoint.point, inRay, n);//refraction ray
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);//closest intersection point of refraction ray with geometries to the geometry
            if (refractedPoint != null)
                result = result.add(calcColor(refractedPoint, refractedRay,
                        level - 1, kkt).scale(kt));//recursion
        }
        return result;

    }

    /**
     * calls the recursive calcColor function
     * @param inRay ray
     * @return color of point
     */
    private Color calcColor(Ray inRay) {
        GeoPoint closestPoint = findClosestIntersection(inRay); //the closest intersection with the geometries point
        if (closestPoint == null){ //If the Ray is not cut with any geometry the pixel will get the background color of the scene
            return _scene.getBackground();
        }
        Color color = calcColor(closestPoint, inRay, MAX_CALC_COLOR_LEVEL, 1.0);//Calculate recursive color in pixels
        color = color.add(_scene.getAmbientLight().getIntensity());//add ambient light
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
     * @return Color
     */
    private  Color adaptiveSuperSampling(Camera camera, double screenDistance, int nx, int ny, double width, double height, int col, int row) {
        List<Ray> rays = camera.constructRaysThroughPixel(nx, ny, col, row, screenDistance, width, height, NUM_OF_SAMPLE_RAYS); // calculate the rays throw the pixel
        int ray1Index = (NUM_OF_SAMPLE_RAYS-1)*NUM_OF_SAMPLE_RAYS+(NUM_OF_SAMPLE_RAYS-1); //the index of the up and right ray
        int ray2Index =(NUM_OF_SAMPLE_RAYS-1)*NUM_OF_SAMPLE_RAYS;  //the index of the up and left ray
        int ray3Index= 0;  //the index of the button and left ray
        int ray4Index = (NUM_OF_SAMPLE_RAYS-1);  // the index of the button and right ray

        Color color = adaptiveSuperSampling(rays, LEVEL_OF_ADAPTIVE, ray1Index, ray2Index, ray3Index, ray4Index); //calculate the color for the pixel
        return color;
    }


    /**
     * Calculates the color of a pixel recursively using adaptive supersampling
     * @param rays list of intersections rays
     * @param level_of_adaptive the level of the recursion
     * @param ray1Index the up and right screen ray
     * @param ray2Index the up and left ray
     * @param ray3Index the button and left ray
     * @param ray4Index the button and right ray
     * @return Color
     */
    private Color adaptiveSuperSampling(List<Ray> rays, int level_of_adaptive,int ray1Index, int ray2Index, int ray3Index, int ray4Index) {
        int numOfAdaptiveRays = 5;

        Ray centerRay = rays.get(rays.size()-1); //get the center screen ray
        Color centerColor=calcColor(centerRay); //get the color of the center
        Ray ray1 = rays.get(ray1Index);  //get the up and right screen ray
        Color color1=calcColor(ray1); //get the color of the up and right
        Ray ray2 = rays.get(ray2Index); //get the up and left ray
        Color color2=calcColor(ray2);//get the color of the up and left
        Ray ray3 = rays.get(ray3Index);//get the button and left ray
        Color color3=calcColor(ray3);//get the color of the button and left
        Ray ray4 = rays.get(ray4Index);//get the button and right ray
        Color color4=calcColor(ray4);//get the color of the button and right
        if (level_of_adaptive == 0)
        {
            //Calculate the average color of the corners and the center
            centerColor = centerColor.add(color1,color2, color3,color4);
            return centerColor.reduce(numOfAdaptiveRays);
        }
        //If the corner color is the same as the center color, returns the center color
        if (color1.isColorsEqual(centerColor) && color2.isColorsEqual(centerColor) && color3.isColorsEqual(centerColor) && color4.isColorsEqual(centerColor))
        {
            return centerColor;
        }
        //Otherwise, for each color that is different from the center, the recursion goes down to the depth of the pixel and sums up
        // the colors until it gets the same color as the center color,
        else {
            if (!color1.isColorsEqual(centerColor)) {
                color1 = color1.add(adaptiveSuperSampling(rays, level_of_adaptive - 1,ray1Index - (NUM_OF_SAMPLE_RAYS+1), ray2Index, ray3Index, ray4Index ));
                color1 = color1.reduce(2);
            }
            if (!color2.isColorsEqual(centerColor)) {;
                color2 = color2.add(adaptiveSuperSampling(rays,level_of_adaptive - 1,ray1Index, ray2Index-(NUM_OF_SAMPLE_RAYS-1), ray3Index, ray4Index));
                color2 = color2.reduce(2);
            }
            if (!color3.isColorsEqual(centerColor)) {
                color3 = color3.add(adaptiveSuperSampling(rays, level_of_adaptive - 1,ray1Index, ray2Index, ray3Index+(NUM_OF_SAMPLE_RAYS+1), ray4Index));
                color3 = color3.reduce(2);
            }
            if (!color4.isColorsEqual(centerColor)) {
                color4 = color4.add(adaptiveSuperSampling(rays, level_of_adaptive - 1,ray1Index, ray2Index, ray3Index, ray4Index+(NUM_OF_SAMPLE_RAYS-1)));
                color4 = color4.reduce(2);
            }
            //Calculate and return the average color
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
        if (intersections == null)//there are no intersection points
            return null;
        GeoPoint closestPoint = getClosestPoint(intersections);//get closest point
        return closestPoint;
    }

    /**
     * construct a refracted ray
     * @param pointGeo GeoPoint- the point on the geometry where color is calculated
     * @param inRay ray
     * @param n the vector exclusion factor from the geometries
     * @return refracted ray
     */
    private Ray constructRefractedRay(Point3D pointGeo, Ray inRay, Vector n) {
        return new Ray(pointGeo, inRay.getDirection(), n);//creates new ray that is scaled by delta- it is moved a little from surface of geometry
    }

    /**
     * construct a reflected ray
     * @param pointGeo GeoPoint- the point on the geometry where color is calculated
     * @param inRay ray
     * @param n the vector exclusion factor from the geometries
     * @return reflected ray
     */
    private Ray constructReflectedRay(Point3D pointGeo, Ray inRay, Vector n) {
        Vector v = inRay.getDirection();
        double vn = v.dotProduct(n);

        if (vn == 0) { //Perpendicular vectors
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
     * @param msg the meesege to print
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
     * @author We helped with a lecturer project, we understood the code
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
            ++col; //Promotes columns
            ++_counter;
            if (col < _maxCols) {//the row did'nt end
                target.row = this.row;
                target.col = this.col;
                if (_print && _counter == _nextCounter) {//promote the percents of the work that done
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            ++row;//Promotes rows
            if (row < _maxRows) { //we didnt finish
                col = 0;
                if (_print && _counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;//promote the percents of the work that done
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
            int percents = nextP(target);//get the next pixel
            if (_print && percents > 0) { //print the percent of work that done
                printMessage(String.format(" %02d%%", _percents));
            }
            if (percents >= 0)//image writer progress started
                return true;
            if (_print) {//print the percent of work that done
                printMessage(String.format(" %02d%%", 100));
            }
            return false;//image writer progress did'nt started
        }
    }

}
