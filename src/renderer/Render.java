
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

    public Render(Scene _scene) {
        this._scene = _scene;
    }

    public Render(Scene scene, ImageWriter imageWriter) {
        this._imageWriter = imageWriter;
        this._scene = scene;
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
                    java.awt.Color pixelColor = calcColor(closestPoint).getColor();
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
     *
     * @return the color intensity
     */
    private Color calcColor(GeoPoint point) {
        Color ambientLight=_scene.getAmbientLight().getIntensity();
        Color emmissionLLight=point.geometry.getEmissionLight();
        Color diffuseLight=new Color(0,0,0);
        Color specularLight=new Color(0,0,0);
        Vector n=point.geometry.getNormal(point.point).normalize();
        Vector v=(point.point).subtract(_scene.getCamera().get_p0()).normalize();
        for(LightSource l:_scene.getLightSources())
        {
            if(Math.signum(n.dotProduct(l.getL(point.point)))==Math.signum(n.dotProduct(v))) {
                diffuseLight = diffuseLight.add(calcDiffusive(point, l));
                specularLight = specularLight.add(calcSpecular(point, l));
            }
        }
        return new Color(ambientLight.add(emmissionLLight,diffuseLight,specularLight));
    }


    private boolean sign(double val) {
        return (val > 0d);
    }

    /**
     * Calculate Specular component of light reflection.
     *
     *
     * <p>
     * Finally, the Phong model has a provision for a highlight, or specular, component, which reflects light in a
     * shiny way. This is defined by [rs,gs,bs](-V.R)^p, where R is the mirror reflection direction vector we discussed
     * in class (and also used for ray tracing), and where p is a specular power. The higher the value of p, the shinier
     * the surface.
     */
    private Color calcSpecular(GeoPoint point,LightSource l)
    {
        double ks=point.geometry.getMaterial().getKs();
        Vector v=(point.point).subtract(_scene.getCamera().get_p0()).normalize();
        Vector d=l.getL(point.point);
        Vector n=point.geometry.getNormal(point.point).normalize();
        Vector r=d.subtract(n.scale(2*d.dotProduct(n)));
        double dotPro=v.scale(-1).dotProduct(r);
        return l.getIntensity(point.point).scale((Math.pow(Math.max(dotPro,0),point.geometry.getMaterial().getnShininess())*ks));
    }

    /**
     * Calculate Diffusive component of light reflection.
     *
     * intensity at the point
     * @return diffusive component of light reflection
     * @author Dan Zilberstein
     * <p>
     * The diffuse component is that dot product n•L that we discussed in class. It approximates light, originally
     * from light source L, reflecting from a surface which is diffuse, or non-glossy. One example of a non-glossy
     * surface is paper. In general, you'll also want this to have a non-gray color value, so this term would in general
     * be a color defined as: [rd,gd,bd](n•L)
     */
    private Color calcDiffusive(GeoPoint point,LightSource l)
    {
        Color inten = l.getIntensity(point.point);
        double kd=point.geometry.getMaterial().getKd();
        double dotPro=(point.geometry.getNormal(point.point)).normalize().dotProduct(l.getL(point.point));
        return inten.scale(Math.abs(kd*dotPro));
    }

}