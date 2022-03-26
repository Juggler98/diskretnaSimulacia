package udalostna.salon.pracoviska;

public class Zamestnanec implements Comparable<Zamestnanec> {

    private Double odpracovanyCas = 0.0;

    Zamestnanec() {

    }

    public void addOdpracovanyCas(double odpracovanyCas) {
        this.odpracovanyCas += odpracovanyCas;
    }

    public double getOdpracovanyCas() {
        return odpracovanyCas;
    }

    @Override
    public int compareTo(Zamestnanec o) {
        return odpracovanyCas.compareTo(o.odpracovanyCas);
    }
}
