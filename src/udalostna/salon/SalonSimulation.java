package udalostna.salon;

import generators.*;
import simCores.EventCore;
import udalostna.salon.events.EventPrichod;
import udalostna.salon.pracoviska.Pracovisko;
import udalostna.salon.zakaznik.ZakaznikSalonu;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class SalonSimulation extends EventCore {

    private final PriorityQueue<ZakaznikSalonu> radRecepcia = new PriorityQueue<>();
    private final LinkedList<ZakaznikSalonu> radUces = new LinkedList<>();
    private final LinkedList<ZakaznikSalonu> radLicenie = new LinkedList<>();

    private final Random seedGenerator = new Random();
    private final RandPoisson randPrichod = new RandPoisson(450, seedGenerator);
    private final RandUniformContinuous randObjednavka = new RandUniformContinuous(200 - 120, 200 + 120, seedGenerator);
    private final RandTriangular randHlbkoveCistenie = new RandTriangular(360, 900, 540, seedGenerator);
    private final RandUniformContinuous randPlatba = new RandUniformContinuous(180 - 50, 180 + 50, seedGenerator);
    private final RandUniformDiscrete randUcetJednoduchy = new RandUniformDiscrete(10 * 60, 30 * 60, seedGenerator);
    private final EmpiricDiscrete[] empiricDiscretesUcesZlozity = {new EmpiricDiscrete(30 * 60, 60 * 60, 0.4), new EmpiricDiscrete(61 * 60, 120 * 60, 0.6)};
    private final RandEmpiricDiscrete randUcesZlozity = new RandEmpiricDiscrete(empiricDiscretesUcesZlozity, seedGenerator);
    private final EmpiricDiscrete[] empiricDiscretesUcesSvadobny = {new EmpiricDiscrete(50 * 60, 60 * 60, 0.2), new EmpiricDiscrete(61 * 60, 100 * 60, 0.3), new EmpiricDiscrete(101 * 60, 150 * 60, 0.5)};
    private final RandEmpiricDiscrete randUcesSvadobny = new RandEmpiricDiscrete(empiricDiscretesUcesSvadobny, seedGenerator);
    private final RandUniformDiscrete randLicenieJednoduche = new RandUniformDiscrete(10 * 60, 25 * 60, seedGenerator);
    private final RandUniformDiscrete randLicenieZlozite = new RandUniformDiscrete(20 * 60, 100 * 60, seedGenerator);
    private final Random randPercentageTypZakaznika = new Random(seedGenerator.nextLong());
    private final Random randPercentageCiseniePleti = new Random(seedGenerator.nextLong());

    private final Random randPercentageTypUcesu = new Random(seedGenerator.nextLong());
    private final Random randPercentageTypLicenia = new Random(seedGenerator.nextLong());

    private Pracovisko pracoviskoRecepcia;
    private Pracovisko pracoviskoUcesy;
    private Pracovisko pracoviskoLicenie;

    private final int endTime;

    public int[] statsVykonov = new int[10];
    private final double[] statsAllVykonov = new double[10];
    private final String[] statsNames = {"zadaneUcesy", "spraveneUcesy", "zadaneLicenia", "spraveneLicenia", "zadaneUcesyAjLicenia", "spraveneUcesyAjLicenia", "zadaneCistenie", "spraveneCistenie", "pocetZadanychObjednavok", "pocetObsluzenychZakaznikov"};

    public double casStravenyVSalone = 0;
    private double celkovyPriemerCasuVSalone = 0;

    public double dlzkaCakania = 0;
    private double celkovaDlzkaCakania = 0;

    private int pocetReplikacii = 0;

    private final int pocetRecepcnych;
    private final int pocetKadernicok;
    private final int pocetKozmeticiek;

    Main main;

    public SalonSimulation(int endTime, int pocetRecepcnych, int pocetKadernicok, int pocetKozmeticiek, Main main) {
        this.endTime = endTime;
        this.pocetRecepcnych = pocetRecepcnych;
        this.pocetKadernicok = pocetKadernicok;
        this.pocetKozmeticiek = pocetKozmeticiek;
        this.main = main;
    }

    @Override
    public void beforeReplications() {

    }

    @Override
    public void beforeReplication() {
        radRecepcia.clear();
        radLicenie.clear();
        radUces.clear();

        Arrays.fill(statsVykonov, 0);

        casStravenyVSalone = 0;
        dlzkaCakania = 0;

        pracoviskoRecepcia = new Pracovisko(pocetRecepcnych);
        pracoviskoUcesy = new Pracovisko(pocetKadernicok);
        pracoviskoLicenie = new Pracovisko(pocetKozmeticiek);

        ZakaznikSalonu zakaznikSalonu = new ZakaznikSalonu(randPrichod.nextValue());
        EventPrichod eventPrichod = new EventPrichod(zakaznikSalonu, zakaznikSalonu.getCasPrichodu(), this);
        kalendarUdalosti.add(eventPrichod);
    }

    @Override
    public void replication() {
        this.simulateEvents(endTime);
    }

    @Override
    public void afterReplication() {
        celkovyPriemerCasuVSalone += casStravenyVSalone / statsVykonov[8];
        celkovaDlzkaCakania += dlzkaCakania / statsVykonov[9];
        pocetReplikacii++;

        for (int i = 0; i < statsVykonov.length; i++) {
            statsAllVykonov[i] += statsVykonov[i];
        }
    }

    @Override
    public void afterReplications() {
        for (int i = 0; i < statsVykonov.length; i++) {
            System.out.println(statsNames[i] + ": " + statsAllVykonov[i] / pocetReplikacii);
        }
        System.out.println("PriemernyCas: " + celkovyPriemerCasuVSalone / 3600 / pocetReplikacii + " hod");
        System.out.println("PriemernyCasCakania: " + celkovaDlzkaCakania / 60 / pocetReplikacii + " min");

        main.set(celkovyPriemerCasuVSalone / 3600 / pocetReplikacii, celkovaDlzkaCakania / 60 / pocetReplikacii);
        System.out.println();
    }

    public PriorityQueue<ZakaznikSalonu> getRadRecepcia() {
        return radRecepcia;
    }

    public LinkedList<ZakaznikSalonu> getRadUces() {
        return radUces;
    }

    public LinkedList<ZakaznikSalonu> getRadLicenie() {
        return radLicenie;
    }

    public RandUniformContinuous getRandObjednavka() {
        return randObjednavka;
    }

    public RandTriangular getRandHlbkoveCistenie() {
        return randHlbkoveCistenie;
    }

    public RandUniformContinuous getRandPlatba() {
        return randPlatba;
    }

    public RandUniformDiscrete getRandUcetJednoduchy() {
        return randUcetJednoduchy;
    }

    public RandEmpiricDiscrete getRandUcesZlozity() {
        return randUcesZlozity;
    }

    public RandEmpiricDiscrete getRandUcesSvadobny() {
        return randUcesSvadobny;
    }

    public RandUniformDiscrete getRandLicenieJednoduche() {
        return randLicenieJednoduche;
    }

    public RandUniformDiscrete getRandLicenieZlozite() {
        return randLicenieZlozite;
    }

    public RandPoisson getRandPrichod() {
        return randPrichod;
    }

    public Pracovisko getPracoviskoRecepcia() {
        return pracoviskoRecepcia;
    }

    public Pracovisko getPracoviskoUcesy() {
        return pracoviskoUcesy;
    }

    public Pracovisko getPracoviskoLicenie() {
        return pracoviskoLicenie;
    }

    public Random getRandPercentageTypZakaznika() {
        return randPercentageTypZakaznika;
    }

    public Random getRandPercentageCiseniePleti() {
        return randPercentageCiseniePleti;
    }

    public Random getRandPercentageTypUcesu() {
        return randPercentageTypUcesu;
    }

    public Random getRandPercentageTypLicenia() {
        return randPercentageTypLicenia;
    }

    public int getDlzkaRaduUcesyLicenie() {
        return radLicenie.size() + radUces.size();
    }

    public int getEndTime() {
        return endTime;
    }
}
