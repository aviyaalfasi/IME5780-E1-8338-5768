package renderer;

import elements.Camera;

import geometries.Intersectable;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import scene.Scene;

import java.util.List;

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
                List<Point3D> intersectionPoints = geometries.findIntsersections(ray);
                if (intersectionPoints == null) {
                    _imageWriter.writePixel(column, row, background);
                } else {
                    Point3D closestPoint = getClosestPoint(intersectionPoints);
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
    private Point3D getClosestPoint(List<Point3D> intersectionPoints) {
        Point3D result = null;
        double min = Double.MAX_VALUE;
        Point3D p0 = this._scene.getCamera().get_p0();
        for (Point3D pt : intersectionPoints) {
            double distance = p0.distance(pt);
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
    private java.awt.Color calcColor(Point3D point) {
        return _scene.getAmbientLight().getIntensity().getColor();
    }


}