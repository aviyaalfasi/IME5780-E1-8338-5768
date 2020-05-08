package renderer;

import elements.Camera;
import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import geometries.Intersectable;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * @author aviya and sima
 */

public class Render {
    private Scene _scene;
    private ImageWriter _imageWriter;

    /**
     * constructor for Render that recieves two arguments
     * @param _scene scene
     * @param _imageWriter image writer
     */
    public Render(Scene _scene, ImageWriter _imageWriter) {
        this._scene = _scene;
        this._imageWriter = _imageWriter;
    }

    /**
     * renders an image
     */
    public void renderImage() {
        java.awt.Color background = _scene.getBackground().getColor();
        Camera camera = _scene.getCamera();
        Intersectable geometries = _scene.getGeometries();
        double distance = _scene.getDistance();

        // Number of pixels in the row of View Plane
        int width = (int) _imageWriter.getWidth();
        // Number of pixels in the column of View Plane
        int height = (int) _imageWriter.getHeight();

        // Width of the image
        int Nx = _imageWriter.getNx();
        // Height the image
        int Ny = _imageWriter.getNy();

        Ray ray;
        for (int row = 0; row < Ny; row++) {
            for (int column = 0; column < Nx; column++) {
                ray = camera.constructRayThroughPixel(Nx, Ny, row, column, distance, width, height);
                List<GeoPoint> intersectionPoints = geometries.findIntsersections(ray);
                if (intersectionPoints == null) {
                    _imageWriter.writePixel(column, row, background);
                } else {
                    GeoPoint closestPoint = getClosestPoint(intersectionPoints);
                    _imageWriter.writePixel(column - 1, row - 1, calcColor(closestPoint));
                }
            }
        }
    }

    /**
     * receives list of intersection points and returns the closest one to the camera
     * @param intersectionPoints intersection points with ray
     * @return closest point to camera
     */
    private GeoPoint getClosestPoint(List<GeoPoint> intersectionPoints) {
        GeoPoint result = null;
        double min = Double.MAX_VALUE;
        Point3D p0 = this._scene.getCamera().get_p0();
        for (GeoPoint pt : intersectionPoints) {
            double distance = p0.distance(pt.point);
            if (distance < min) {
                min = distance;
                result = pt;
            }
        }
        return result;
    }

    /**
     * prints a grid on the image
     * @param interval the interval between the lines
     * @param color color of grid
     */
    public void printGrid(int interval, Color color) {
        double rows = this._imageWriter.getNx();
        double columns = _imageWriter.getNy();

        for (int col = 0; col < columns; col++)
            for (int row = 0; row < rows; row++)
                if (col % interval == 0 || row % interval == 0)
                    _imageWriter.writePixel(row, col, color.getColor());

    }

    /**
     * calls the function writeToImage of the image writer in this renderer
     */
    public void writeToImage() {
        _imageWriter.writeToImage();
    }

    /**
     * calculates the color of each point in the view plane
     * @param point the point where color is calculated
     * @return color in received point
     */
    private java.awt.Color calcColor(GeoPoint point) {
        Color result = new Color(_scene.getAmbientLight().getIntensity());
        result = result.add(point.getGeometry().getEmissionLight());

        Vector v = point.getPoint().subtract(_scene.getCamera().get_p0()).normalize();
        Vector n = point.getGeometry().getNormal(point.getPoint());

        Material material = point.getGeometry().getMaterial();
        int nShininess = material.getnShininess();
        double kd = material.getKd();
        double ks = material.getKs();
        if (_scene.getLightSources() != null) {
            for (LightSource lightSource : _scene.getLightSources()) {

                Vector l = lightSource.getL(point.getPoint());
                double nl = alignZero(n.dotProduct(l));
                double nv = alignZero(n.dotProduct(v));

                if (sign(nl) == sign(nv)) {
                    Color ip = lightSource.getIntensity(point.getPoint());
                    result = result.add(
                            calcDiffusive(kd, nl, ip),
                            calcSpecular(ks, l, n, nl, v, nShininess, ip)
                    );
                }
            }
        }
        return result.getColor();
    }

    private boolean sign(double val) {
        return (val > 0d);
    }

    private Color calcSpecular(double ks, Vector l, Vector n, double nl, Vector v, int nShininess, Color ip) {
        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -alignZero(r.dotProduct(v));
        if (minusVR <= 0) return Color.BLACK; // view from direction opposite to r vector
        return ip.scale(ks * Math.pow(minusVR, nShininess));
    }

    private Color calcDiffusive(double kd, double nl, Color ip) {
        if (nl < 0) nl = -nl;
        return ip.scale(nl * kd);
    }
}