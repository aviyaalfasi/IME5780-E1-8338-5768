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

    public AmbientLight get_ambientLight() {
        return _ambientLight;
    }

    public Camera get_camera() {
        return _camera;
    }

    public Color get_background() {
        return _background;
    }

    public double get_distance() {
        return _distance;
    }

    public Geometries get_geometries() {
        return _geometries;
    }

    public String get_name() {
        return _name;
    }

    public void set_ambientLight(AmbientLight _ambientLight) {
        this._ambientLight = _ambientLight;
    }

    public void set_distance(double _distance) {
        this._distance = _distance;
    }

    public void set_background(Color _background) {
        this._background = _background;
    }

    public void set_camera(Camera _camera) {
        this._camera = _camera;
    }

    void addGeometries(Intersectable... geometries) {
        _geometries.add(geometries);
    }
}
