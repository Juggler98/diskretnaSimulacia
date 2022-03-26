package udalostna.salon;

import udalostna.gui.EventGUI;

import javax.swing.*;

public class Main {

    private double cas = 0;
    private double casCakania = 0;

    public static void main(String[] args) {
        //new Main();

        SwingUtilities.invokeLater(EventGUI::new);
    }

    Main() {
        boolean a = true;
        if (a) {
            int min = Integer.MAX_VALUE;
            int minI = Integer.MAX_VALUE;
            int minJ = Integer.MAX_VALUE;
            int minK = Integer.MAX_VALUE;
            for (int i = 1; i <= 10; i++) {
                for (int j = 1; j <= 10; j++) {
                    for (int k = 1; k <= 10; k++) {
                        System.out.printf("%d %d %d\n", i, j, k);
                        SalonSimulation salonSimulation = new SalonSimulation((17 - 9) * 3600, i, j, k);
                        salonSimulation.simulate(100);
                        if (cas <= 3 && casCakania <= 4) {
                            if (i + j + k < min) {
                                min = i + j + k;
                                minI = i;
                                minJ = j;
                                minK = k;
                                System.out.printf("%d %d %d\n", minI, minJ, minK);
                                System.out.println("-----------------------------------------------------------------");
                            }
                        }
                    }
                }
            }
            System.out.printf("%d %d %d\n", minI, minJ, minK);
        } else {
            SalonSimulation salonSimulation = new SalonSimulation((17 - 9) * 3600, 2, 6, 5);
            salonSimulation.simulate(10000);
        }

    }

    public void set(double cas, double casCakania) {
        this.cas = cas;
        this.casCakania = casCakania;
    }


}
