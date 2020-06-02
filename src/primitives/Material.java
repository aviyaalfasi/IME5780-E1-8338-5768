package primitives;

public class Material {
    double _kD;
    double _kS;
    int _nShininess;
    double _kT;
    double _kR;


    public Material(double _kD, double _kS, int _nShininess) {
       this(_kD, _kS, _nShininess, 0, 0);
    }

    public Material(double _kD, double _kS,int _nShininess,double _kT,double _kR) {
        this._kD = _kD;
        this._kS = _kS;
        this._kT=_kT;
        this._kR=_kR;
        this._nShininess = _nShininess;
    }

    public double getKT() { return _kT;}

    public double getKR() { return _kR; }

    public double getKd() {
        return _kD;
    }

    public double getKs() {
        return _kS;
    }

    public int getnShininess() {
        return _nShininess;
    }
}
