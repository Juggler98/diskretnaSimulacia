package monteCarloParking;

import simCores.MonteCarlo;

import java.util.ArrayList;
import java.util.Random;

public class ParkingSimulation extends MonteCarlo {

    Random randomSeed = new Random();
    Random randomK;
    Random[] randoms;

    private final int n;
    private final int strategia;
    private int result = -1;
    ArrayList<Integer> freeSlots;
    private final GUI gui;

    public ParkingSimulation(int n, int strategia, GUI gui) {
        this.n = n;
        this.strategia = strategia;
        this.gui = gui;
    }

    @Override
    public void beforeReplications() {
        gui.prepare(n);
        randoms = new Random[n];
        randomK = new Random(randomSeed.nextInt());
        for (int i = 0; i < n; i++) {
            randoms[i] = new Random(randomSeed.nextInt());
        }
        freeSlots = new ArrayList<>(n);
    }

    @Override
    public void beforeReplication() {
        freeSlots.clear();
        for (int i = 1; i <= n; i++) {
            freeSlots.add(i);
        }
    }

    @Override
    public void replication() {
        int k = randomK.nextInt(n) + 1;
        for (int i = 0; i < k; i++) {
            freeSlots.remove(randoms[freeSlots.size() - 1].nextInt(freeSlots.size()));
        }

        int zaciatok;
        if (strategia == 1) {
            zaciatok = n;
        } else if (strategia == 2) {
            zaciatok = n - (int) Math.ceil(2.0 * n / 3);
        } else {
            zaciatok = n - (int) Math.ceil(n / 2.0);
        }

        boolean zaparkovane = false;

        for (int i = freeSlots.size() - 1; i >= 0; i--) {
            Integer freeSlot = freeSlots.get(i);
            if (freeSlot != 3 && freeSlot != 6 && freeSlot != 9) {
                if (freeSlot <= zaciatok) {
                    result = freeSlot;
                    zaparkovane = true;
                    break;
                }
            }
        }
        if (!zaparkovane) {
            result = 3 * n;
        }
    }

    @Override
    public void afterReplication() {
        gui.calculate(result, getIteration());
    }

    @Override
    public void afterReplications() {
        gui.showResults();
    }

}
