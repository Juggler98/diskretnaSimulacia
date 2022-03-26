package udalostna.salon.zakaznik;

public class ZakaznikSalonu implements Comparable<ZakaznikSalonu> {

    private final double casPrichodu;
    private double casOdchodu;
    private double casCakania;
    private boolean obsluzeny = false;
    private TypZakaznika typZakaznika;
    private boolean hlbkoveLicenie = false;

    public ZakaznikSalonu(double casPrichodu) {
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

    public boolean isObsluzeny() {
        return obsluzeny;
    }

    public void setObsluzeny() {
        this.obsluzeny = true;
    }

    public TypZakaznika getTypZakaznika() {
        return typZakaznika;
    }

    public boolean isHlbkoveLicenie() {
        return hlbkoveLicenie;
    }

    public void setTypZakaznika(TypZakaznika typZakaznika) {
        this.typZakaznika = typZakaznika;
    }

    public void setHlbkoveLicenie(boolean hlbkoveLicenie) {
        this.hlbkoveLicenie = hlbkoveLicenie;
    }

    public double getCasOdchodu() {
        return casOdchodu;
    }

    public void setCasOdchodu(double casOdchodu) {
        this.casOdchodu = casOdchodu;
    }

    @Override
    public int compareTo(ZakaznikSalonu o) {
        if (obsluzeny && o.obsluzeny)
            return 0;
        if (obsluzeny)
            return -1;
        if (o.obsluzeny)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "ZakaznikSalonu{" +
                "casPrichodu=" + casPrichodu +
                " casPrichodu=" + casPrichodu / 3600 +
                ", casCakania=" + casCakania +
                ", obsluzeny=" + obsluzeny +
                ", typZakaznika=" + typZakaznika +
                ", hlbkoveLicenie=" + hlbkoveLicenie +
                '}';
    }
}
