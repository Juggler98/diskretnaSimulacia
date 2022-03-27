package udalostna.salon.pracoviska;

import java.util.PriorityQueue;

public class Pracovisko {

    private final PriorityQueue<Zamestnanec> volnyZamestnanci = new PriorityQueue<>();
    private final Zamestnanec[] zamestnanci;

    public Pracovisko(int pocetZamestnancov) {
        zamestnanci = new Zamestnanec[pocetZamestnancov];
        for (int i = 0; i < pocetZamestnancov; i++) {
            zamestnanci[i] = new Zamestnanec();
            volnyZamestnanci.add(zamestnanci[i]);
        }
    }

    public int getPocetZamestnancov() {
        return zamestnanci.length;
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

    public Zamestnanec getZamestnanec(int i) {
        return zamestnanci[i];
    }

}
