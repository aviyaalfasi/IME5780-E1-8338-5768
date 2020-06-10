package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import static primitives.Util.isZero;

/**
 * @author aviya and sima
 */

public class Camera {
    Point3D _p0;
    Vector _vUp;
    Vector _vRight;
    Vector _vTo;
    private static final Random rnd = new Random();


    /**
     * @return the point where camera is located
     */
    public Point3D get_p0() {
        return _p0;
    }

    /**
     * @return vector vUp
     */
    public Vector get_vUp() {
        return _vUp;
    }

    /**
     * @return vector vRight
     */
    public Vector get_vRight() {
        return _vRight;
    }

    /**
     * @return vector vTowards
     */
    public Vector get_vTo() {
        return _vTo;
    }

    /**
     * constructor for camera that receives the point where camera is locted, vUp, and vTowards, and creates vRight by cross product of both vectors
     * @param _p0 camera's location
     * @param _vTo
     * @param _vUp
     */
    public Camera(Point3D _p0, Vector _vTo, Vector _vUp) {

        //if the the vectors are not orthogonal, throw exception.
        if(Util.isZero(_vTo.dotProduct(_vUp))){
            this._p0 =  new Point3D(_p0);
            this._vTo = _vTo.normalized();
            this._vUp = _vUp.normalized();
            _vRight = _vTo.crossProduct(_vUp).normalized();
        }
    }

    public Ray constructRayThroughPixel(int nX, int nY, int j, int i, double screenDistance,
                                        double screenWidth, double screenHeight)
    {
        if (isZero(screenDistance))
        {
            throw new IllegalArgumentException("distance cannot be 0");
        }
        Point3D Pc = new Point3D( _p0.add(_vTo.scale(screenDistance)));
        double Ry = screenHeight/nY;
        double Rx = screenWidth/nX;
        double yi =  ((i - nY/2d)*Ry + Ry/2d);
        double xj=   ((j - nX/2d)*Rx + Rx/2d);

        Point3D Pij = Pc;

        if (!Util.isZero(xj))
        {
            Pij = Pij.add(_vRight.scale(xj));
        }
        if (!Util.isZero(yi))
        {
            Pij = Pij.add(_vUp.scale(-yi));
        }
        Vector Vij = Pij.subtract(_p0);
        return new Ray(_p0,Vij);
    }



    public List<Ray> constructRaysThroughPixel(int nX, int nY, int j, int i, double screenDistance,
                                               double screenWidth, double screenHeight, int num_of_sample_rays)
    {
        if (isZero(screenDistance))
        {
            throw new IllegalArgumentException("distance cannot be 0");
        }

        List<Ray> sample_rays = new ArrayList<>();

        double Ry = screenHeight/nY;
        double Rx = screenWidth/nX;
        double yi =  ((i - nY/2d)*Ry);
        double xj=   ((j - nX/2d)*Rx);

        for (int row = 0; row < num_of_sample_rays; ++row) {
            for (int column = 0; column < num_of_sample_rays; ++column) {
                sample_rays.add(constructRaysThroughPixel(num_of_sample_rays, num_of_sample_rays,yi, xj, row, column,screenDistance, Rx, Ry));
            }
        }
        return sample_rays;
    }

    private Ray constructRaysThroughPixel(int nX, int nY, double yi, double xj, int j, int i, double screenDistance,
                                         double pixelWidth, double pixelHeight)
    {

        Point3D Pc = new Point3D( _p0.add(_vTo.scale(screenDistance)));
        double Ry = pixelHeight/nY;
        double Rx = pixelWidth/nX;
        double y_sample_i =  (i *Ry + Ry/2d);
        double x_sample_j=   (j *Rx + Rx/2d);

        Point3D Pij = Pc;

        if (!Util.isZero(x_sample_j + xj))
        {
            Pij = Pij.add(_vRight.scale(x_sample_j + xj));
        }
        if (!Util.isZero(y_sample_i + yi))
        {
            Pij = Pij.add(_vUp.scale(-y_sample_i -yi ));
        }
        Vector Vij = Pij.subtract(_p0);
        return new Ray(_p0,Vij);
    }
}