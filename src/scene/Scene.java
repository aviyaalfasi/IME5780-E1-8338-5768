package scene;

import elements.AmbientLight;
import elements.Camera;
import geometries.Geometries;
import geometries.Intersectable;
import primitives.Color;

public class Scene {
    String _name;
    Color _background;
    AmbientLight _ambientLight;
    Geometries _geometries;
    Camera _camera;
    double _distance;

    public Scene(String name){
        _name = name;
        _geometries = new Geometries();
    }

    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    public Camera getCamera() {
        return _camera;
    }

    public Color getBackground() {
        return _background;
    }

    public double getDistance() {
        return _distance;
    }

    public Geometries getGeometries() {
        return _geometries;
    }

    public String getName() {
        return _name;
    }

    public void setAmbientLight(AmbientLight _ambientLight) {
        this._ambientLight = _ambientLight;
    }

    public void setDistance(double _distance) {
        this._distance = _distance;
    }

    public void setBackground(Color _background) {
        this._background = _background;
    }

    public void setCamera(Camera _camera) {
        this._camera = _camera;
    }

    public void addGeometries(Intersectable... geometries) {
        _geometries.add(geometries);
    }
}
