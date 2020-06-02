
package renderer;

import elements.*;
import geometries.*;
import primitives.*;
import geometries.Intersectable.GeoPoint;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

public class Render {
    private Scene _scene;
    private ImageWriter _imageWriter;
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 50;
    private static final double MIN_CALC_COLOR_K = 0.0000001;


    public Render(Scene _scene) {
        this._scene = _scene;
    }

    public Render(Scene scene, ImageWriter imageWriter) {
        this._imageWriter = imageWriter;
        this._scene = scene;
    }

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
    private double transparency(LightSource ls, Vector l, Vector n, GeoPoint geopoint){
        Vector lightDirection = l.scale(-1); // from point to light source

        Vector delta = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : -DELTA);
        Point3D point = geopoint.getPoint().add(delta);
        Ray lightRay = new Ray(point, lightDirection, n);

        List<GeoPoint> intersections = _scene.getGeometries().findIntsersections(lightRay);
        if (intersections == null) {
            return 1.0;
        }
        double lightDistance = ls.getDistance(geopoint.getPoint());
        double ktr = 1.0;
        for (GeoPoint gp2 : intersections) {
            if (alignZero(gp2.getPoint().distance(geopoint.getPoint()) - lightDistance) <= 0)
                ktr *= geopoint.geometry.getMaterial().getKT();
            if (ktr < MIN_CALC_COLOR_K) return 0.0;

        }
        return ktr;
    }
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
    public Scene get_scene() {
        return _scene;
    }

    /**
     * Filling the buffer according to the geometries that are in the scene.
     * This function does not creating the picture file, but create the buffer pf pixels
     */
    public void renderImage() {
        java.awt.Color background = _scene.getBackground().getColor();
        Camera camera = _scene.getCamera();
        Intersectable geometries = _scene.getGeometries();
        double distance = _scene.getDistance();

        //width and height are the number of pixels in the rows
        //and columns of the view plane
        int width = (int) _imageWriter.getWidth();
        int height = (int) _imageWriter.getHeight();

        //Nx and Ny are the width and height of the image.
        int Nx = _imageWriter.getNx(); //columns
        int Ny = _imageWriter.getNy(); //rows
        //pixels grid
        for (int row = 0; row < Ny; ++row) {
            for (int column = 0; column < Nx; ++column) {
                Ray ray = camera.constructRayThroughPixel(Nx, Ny, column, row, distance, width, height);
                List<GeoPoint> intersectionPoints = geometries.findIntsersections(ray);
                if (intersectionPoints == null) {
                    _imageWriter.writePixel(column, row, background);
                } else {
                    GeoPoint closestPoint = getClosestPoint(intersectionPoints);
                    java.awt.Color pixelColor = calcColor(closestPoint, ray).getColor();
                    _imageWriter.writePixel(column, row, pixelColor);
                }
            }
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

    public void writeToImage() {
        _imageWriter.writeToImage();
    }

    /**
     * Calculate the color intensity in a point
     *
     * @return the color intensity
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

    private Color calcColor(GeoPoint geoPoint, Ray inRay) {
        Color color = calcColor(geoPoint, inRay, MAX_CALC_COLOR_LEVEL, 1.0);
        color = color.add(_scene.getAmbientLight().getIntensity());
        return color;
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

        GeoPoint closestPoint = null;
        double closestDistance = Double.MAX_VALUE;
        Point3D ray_p0 = ray.getPoint();

        List<GeoPoint> intersections = _scene.getGeometries().findIntsersections(ray);
        if (intersections == null)
            return null;

        for (GeoPoint geoPoint : intersections) {
            double distance = ray_p0.distance(geoPoint.getPoint());
            if (distance < closestDistance) {
                closestPoint = geoPoint;
                closestDistance = distance;
            }
        }
        return closestPoint;
    }

    private Ray constructRefractedRay(Point3D pointGeo, Ray inRay, Vector n) {
        return new Ray(pointGeo, inRay.getDirection(), n);
    }

    private Ray constructReflectedRay(Point3D pointGeo, Ray inRay, Vector n) {
        //𝒓=𝒗 −𝟐∙(𝒗∙𝒏)∙𝒏
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
     * @author Dan Zilberstein
     * <p>
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
     * The diffuse component is that dot product n•L that we discussed in class. It approximates light, originally
     * from light source L, reflecting from a surface which is diffuse, or non-glossy. One example of a non-glossy
     * surface is paper. In general, you'll also want this to have a non-gray color value,
     * so this term would in general be a color defined as: [rd,gd,bd](n•L)
     */
    private Color calcDiffusive(double kd, double nl, Color ip) {
        if (nl < 0) {
            nl = -nl;
        }

        return ip.scale(nl * kd);
    }

    private boolean sign(double val) {
        return (val > 0d);
    }

}