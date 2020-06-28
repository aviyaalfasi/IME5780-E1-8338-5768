package primitives;
/**
 * Class Material represents the material of an object in the scene, its shininess, reflectivenes, transparency.
 * @author Aviya and Sima
 */
public class Material {
    double _kD;
    double _kS;
    int _nShininess;
    double _kT;
    double _kR;

    /**
     * Constructor for material
     * @param _kD Diffusion factor of material
     * @param _kS Specular factor of material
     * @param _nShininess Shininess level of material
     */
    public Material(double _kD, double _kS, int _nShininess) {
        this(_kD, _kS, _nShininess, 0, 0);
    }

    /**
     * Constructor for material
     * @param _kD Diffusion factor of material
     * @param _kS Specular factor of material
     * @param _nShininess Shininess level of material
     * @param _kT Transparency factor of material
     * @param _kR Reflectance factor of material
     */
    public Material(double _kD, double _kS,int _nShininess,double _kT,double _kR) {
        this._kD = _kD;
        this._kS = _kS;
        this._kT=_kT;
        this._kR=_kR;
        this._nShininess = _nShininess;
    }

    /**
     * @return Transparency factor of material
     */
    public double getKT() { return _kT;}

    /**
     * @return Reflectance factor of material
     */
    public double getKR() { return _kR; }

    /**
     * @return Diffusion factor of material
     */
    public double getKd() {
        return _kD;
    }

    /**
     * @return Specular factor of material
     */
    public double getKs() {
        return _kS;
    }

    /**
     * @return Shininess of material
     */
    public int getnShininess() {
        return _nShininess;
    }
}
