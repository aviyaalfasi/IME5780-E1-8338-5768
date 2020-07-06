package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

/**
 * Intersectable interface- all geometries implement it.
 * @author Sima and Aviya
 */
public interface Intersectable {
    /**
     * find all intersections of recieved ray with the geometries in the scene
     * @param ray the ray to find intersection points with
     * @return list of GeopPoints- intersection points with the geometries they are on
     */
    List<GeoPoint> findIntsersections(Ray ray);

    /**
     * inner class GeoPoint includes a point and the geometry it is on
     */
    public static class GeoPoint {
        public Geometry geometry;
        public Point3D point;

        /**
         * constructor that recieves both parameters in GeoPoint
         * @param _geometry geometry
         * @param _point point on geometry
         */
        public GeoPoint(Geometry _geometry, Point3D _point) {
            this.geometry = _geometry;
            this.point = _point;
        }

        /**
         * @return geometry of GeoPoint
         */
        public Geometry getGeometry() {
            return geometry;
        }

        /**
         * @return point of GeoPoint
         */
        public Point3D getPoint() {
            return point;
        }

        /**
         * @param o to equal too
         * @return  whether recieved GeoPoint o is equal to this one
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return Objects.equals(geometry, geoPoint.geometry) &&
                    Objects.equals(point, geoPoint.point);
        }

        @Override
        public int hashCode() {
            return Objects.hash(geometry, point);
        }
    }

}
