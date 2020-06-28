package scene;

import elements.AmbientLight;
import elements.Camera;
import elements.LightSource;
import geometries.Geometries;
import geometries.Intersectable;
import primitives.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class Scene represents a scene to be created.
 * @author aviya and sima
 */

public class Scene {
    String _name;
    Color _background;
    AmbientLight _ambientLight;
    Geometries _geometries;
    Camera _camera;
    double _distance;
    List<LightSource> _lights = null;

    /**
     * constructor for Scene- it receives the name of the scene and creates an empty list of geometries in the scene
     * @param name scene name
     */
    public Scene(String name){
        _name = name;
        _geometries = new Geometries();
        _lights = new LinkedList<LightSource>();
    }

    /**
     * get ambient light value
     * @return ambient light value
     */
    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    /**
     * get camera value
     * @return camera value
     */
    public Camera getCamera() {
        return _camera;
    }

    /**
     * get background color
     * @return background color
     */
    public Color getBackground() {
        return _background;
    }

    /**
     * get screen distance from camera
     * @return screen distance from camera
     */
    public double getDistance() {
        return _distance;
    }

    /**
     * get list of geometries in scene
     * @return list of geometries in scene
     */
    public Geometries getGeometries() {
        return _geometries;
    }

    /**
     * get scene name
     * @return scene name
     */
    public String getName() {
        return _name;
    }

    /**
     * set value to ambient light
     * @param _ambientLight value for ambient light
     */
    public void setAmbientLight(AmbientLight _ambientLight) {
        this._ambientLight = _ambientLight;
    }

    /**
     * set screen distance from camera
     * @param _distance distance from camera
     */
    public void setDistance(double _distance) {
        this._distance = _distance;
    }

    /**
     * set backgrond color
     * @param _background backgrond color
     */
    public void setBackground(Color _background) {
        this._background = _background;
    }

    /**
     * set camera value
     * @param _camera camera value
     */
    public void setCamera(Camera _camera) {
        this._camera = _camera;
    }

    /**
     * receives a geometry or a few geometries and adds them to the list of geometries in scene
     * @param geometries geometries to add
     */
    public void addGeometries(Intersectable... geometries) {
        _geometries.add(geometries);
    }

    /**
     * @return light sources in scene
     */
    public List<LightSource> getLightSources() {
        return _lights;
    }

    /**
     * add a light to list of light sources in scene
     * @param lights light or lights to add
     */
    public void addLights(LightSource... lights) {
        for (LightSource light:lights) {
            _lights.add(light);
        }

    }
}
