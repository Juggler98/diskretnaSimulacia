package udalostna.salon.zakaznik;

import java.util.Arrays;

public class ZakaznikSalonu implements Comparable<ZakaznikSalonu> {

    private final double casPrichodu;
    private double casOdchodu;
    private final double[] casZaciatkuObsluhy = new double[5]; //Objednavka, Uces, HlbkoveCistenie, Licenie,  Platba
    private StavZakaznika stavZakaznika;

    private boolean obsluzeny = false;
    private TypZakaznika typZakaznika;
    private boolean hlbkoveLicenie = false;
    private boolean goToHlbkoveLicenie = false;

    public ZakaznikSalonu(double casPrichodu) {
        this.casPrichodu = casPrichodu;
        Arrays.fill(casZaciatkuObsluhy, 0.0);
    }

    public double getCasPrichodu() {
        return casPrichodu;
    }

    public void setCasZaciatkuObsluhy(int i, double casCakania) {
        this.casZaciatkuObsluhy[i] = casCakania;
    }

    public double getCasZaciatkuObsluhy(int i) {
        return casZaciatkuObsluhy[i];
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

    public boolean isGoToHlbkoveLicenie() {
        return goToHlbkoveLicenie;
    }

    public boolean isHlbkoveLicenie() {
        return hlbkoveLicenie;
    }

    public void setHlbkoveLicenie(boolean hlbkoveLicenie) {
        this.hlbkoveLicenie = hlbkoveLicenie;
    }

    public void setTypZakaznika(TypZakaznika typZakaznika) {
        this.typZakaznika = typZakaznika;
    }

    public void setGoToHlbkoveLicenie(boolean goToHlbkoveLicenie) {
        this.goToHlbkoveLicenie = goToHlbkoveLicenie;
    }

    public double getCasOdchodu() {
        return casOdchodu;
    }

    public void setCasOdchodu(double casOdchodu) {
        this.casOdchodu = casOdchodu;
    }

    public void setStavZakaznika(StavZakaznika stavZakaznika) {
        this.stavZakaznika = stavZakaznika;
    }

    public StavZakaznika getStavZakaznika() {
        return stavZakaznika;
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
                " casPrichodu=" + casPrichodu / 3600 + " hod" +
                ", obsluzeny=" + obsluzeny +
                ", typZakaznika=" + typZakaznika +
                ", hlbkoveLicenie=" + goToHlbkoveLicenie +
                '}';
    }
}
