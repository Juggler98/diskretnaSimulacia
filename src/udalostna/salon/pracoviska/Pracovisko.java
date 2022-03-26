package udalostna.salon.pracoviska;

import java.util.PriorityQueue;

public class Pracovisko {

    private final int pocetZamestnancov;
    private final PriorityQueue<Zamestnanec> volnyZamestnanci = new PriorityQueue<>();

    public Pracovisko(int pocetZamestnancov) {
        this.pocetZamestnancov = pocetZamestnancov;
        for (int i = 0; i < pocetZamestnancov; i++) {
            volnyZamestnanci.add(new Zamestnanec());
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

}
