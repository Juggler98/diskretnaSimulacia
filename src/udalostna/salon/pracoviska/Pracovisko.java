package udalostna.salon.pracoviska;

import java.util.PriorityQueue;

public class Pracovisko {

    private final int pocetZamestnancov;
    private final PriorityQueue<Zamestnanec> volnyZamestnanci = new PriorityQueue<>();
    private final Zamestnanec[] zamestnanci;

    public Pracovisko(int pocetZamestnancov) {
        this.pocetZamestnancov = pocetZamestnancov;
        zamestnanci = new Zamestnanec[pocetZamestnancov];
        for (int i = 0; i < pocetZamestnancov; i++) {
            zamestnanci[i] = new Zamestnanec();
            volnyZamestnanci.add(zamestnanci[i]);
        }
    }

    public int getPocetZamestnancov() {
        return pocetZamestnancov;
    }

    public Zamestnanec obsadZamestnanca() {
        return volnyZamestnanci.poll();
    }

    public void uvolniZamestnanca(Zamestnanec zamestnanec) {
        volnyZamestnanci.add(zamestnanec);
    }

    public boolean jeNiektoVolny() {
        return !volnyZamestnanci.isEmpty();
    }

    public int pocetZamestnancov() {
        return zamestnanci.length;
    }

    public Zamestnanec getZamestnanec(int i) {
        return zamestnanci[i];
    }

}
