package udalostna.stanok;

public class Zakaznik {

    private double casPrichodu;
    private double casCakania;

    public Zakaznik(double casPrichodu) {
        this.casPrichodu = casPrichodu;
    }

    public double getCasPrichodu() {
        return casPrichodu;
    }

    public void setCasCakania(double casCakania) {
        this.casCakania = casCakania;
    }

    public double getCasCakania() {
        return casCakania;
    }

    @Override
    public String toString() {
        return "Zakaznik{" +
                "casPrichodu=" + casPrichodu +
                ", casCakania=" + casCakania +
                '}';
    }
}