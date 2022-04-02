package udalostna.salon;

import generators.*;
import simCores.EventCore;
import udalostna.salon.events.EventPrichod;
import udalostna.salon.events.EventZatvorenie;
import udalostna.salon.pracoviska.Pracovisko;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.StavZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

import java.util.*;

public class SalonSimulation extends EventCore {

    private final PriorityQueue<ZakaznikSalonu> radRecepcia = new PriorityQueue<>();
    private final LinkedList<ZakaznikSalonu> radUces = new LinkedList<>();
    private final LinkedList<ZakaznikSalonu> radLicenie = new LinkedList<>();

    private final Random seedGenerator = new Random();
    private final RandExponential randPrichod = new RandExponential(450, seedGenerator);
    private final RandUniformContinuous randObjednavka = new RandUniformContinuous(200 - 120, 200 + 120, seedGenerator);
    private final RandTriangular randHlbkoveCistenie = new RandTriangular(360, 900, 540, seedGenerator);
    private final RandUniformContinuous randPlatba = new RandUniformContinuous(180 - 50, 180 + 50, seedGenerator);
    private final RandUniformDiscrete randUcesJednoduchy = new RandUniformDiscrete(10, 30, seedGenerator);
    private final EmpiricDiscrete[] empiricDiscretesUcesZlozity = {new EmpiricDiscrete(30, 60, 0.4), new EmpiricDiscrete(61, 120, 0.6)};
    private final RandEmpiricDiscrete randUcesZlozity = new RandEmpiricDiscrete(empiricDiscretesUcesZlozity, seedGenerator);
    private final EmpiricDiscrete[] empiricDiscretesUcesSvadobny = {new EmpiricDiscrete(50, 60, 0.2), new EmpiricDiscrete(61, 100, 0.3), new EmpiricDiscrete(101, 150, 0.5)};
    private final RandEmpiricDiscrete randUcesSvadobny = new RandEmpiricDiscrete(empiricDiscretesUcesSvadobny, seedGenerator);
    private final RandUniformDiscrete randLicenieJednoduche = new RandUniformDiscrete(10, 25, seedGenerator);
    private final RandUniformDiscrete randLicenieZlozite = new RandUniformDiscrete(20, 100, seedGenerator);
    private final Random randPercentageTypZakaznika = new Random(seedGenerator.nextLong());
    private final Random randPercentageCiseniePleti = new Random(seedGenerator.nextLong());
    private final Random randPercentageTypUcesu = new Random(seedGenerator.nextLong());
    private final Random randPercentageTypLicenia = new Random(seedGenerator.nextLong());

    private Pracovisko pracoviskoRecepcia;
    private Pracovisko pracoviskoUcesy;
    private Pracovisko pracoviskoLicenie;

    private final ArrayList<Zamestnanec> zamestnanci = new ArrayList<>();
    private final ArrayList<ZakaznikSalonu> zakaznici = new ArrayList<>();

    private final int endTime;
    private int pocetReplikacii = 0;

    private final int[] statsVykonov = new int[10];
    private final double[] statsAllVykonov = new double[10];
    private final String[] statsNames = {"Zadané účesy", "Spravené účesy", "Zadané Líčenia", "Spravené Líčenia", "Zadané účesy aj líčenia", "Spravené účesy aj líčenia", "Zadané čistenia", "Spravené čistenia", "Zadané objednávky", "Dokončené objednávky", "Čas na objednávku", "Čas v sálone", "Čas účesu"};

    private final double[] casy = new double[5]; //casStravenyVSalone, dlzkaCakaniaNaObjednavku, robenieUcesov
    private final double[] celkoveCasy = new double[5];

    private final double[] dlzkyRadov = new double[3]; //recepcia, ucesy, licenie
    private final double[] celkoveDlzkyRadov = new double[3];

    private final double[] chikvadrat = new double[2];
    private int n = 0;

    private final int pocetRecepcnych;
    private final int pocetKadernicok;
    private final int pocetKozmeticiek;

    private int pocetObsluhovanychRecepcia = 0;

    public SalonSimulation(int endTime, int pocetRecepcnych, int pocetKadernicok, int pocetKozmeticiek) {
        this.endTime = endTime;
        this.pocetRecepcnych = pocetRecepcnych;
        this.pocetKadernicok = pocetKadernicok;
        this.pocetKozmeticiek = pocetKozmeticiek;
    }

    @Override
    public void beforeReplications() {

    }

    @Override
    public synchronized void beforeReplication() {

        radRecepcia.clear();
        radLicenie.clear();
        radUces.clear();

        Arrays.fill(statsVykonov, 0);
        Arrays.fill(casy, 0);
        Arrays.fill(dlzkyRadov, 0);

        pocetObsluhovanychRecepcia = 0;

        this.setSimTimeToZero();

        pracoviskoRecepcia = new Pracovisko(pocetRecepcnych);
        pracoviskoUcesy = new Pracovisko(pocetKadernicok);
        pracoviskoLicenie = new Pracovisko(pocetKozmeticiek);

        zamestnanci.clear();
        zakaznici.clear();
        for (int i = 0; i < pocetRecepcnych; i++) {
            zamestnanci.add(pracoviskoRecepcia.getZamestnanec(i));
        }
        for (int i = 0; i < pocetKadernicok; i++) {
            zamestnanci.add(pracoviskoUcesy.getZamestnanec(i));
        }
        for (int i = 0; i < pocetKozmeticiek; i++) {
            zamestnanci.add(pracoviskoLicenie.getZamestnanec(i));
        }

        ZakaznikSalonu zakaznikSalonu = new ZakaznikSalonu(randPrichod.nextValue(), 1);
        zakaznici.add(zakaznikSalonu);
        zakaznikSalonu.setStavZakaznika(StavZakaznika.PRICHOD);
        EventPrichod eventPrichod = new EventPrichod(zakaznikSalonu, zakaznikSalonu.getCasPrichodu(), this);
        EventZatvorenie zatvorenie = new EventZatvorenie(getEndTime() + 1, this);
        addToKalendar(zatvorenie);
        this.addToKalendar(eventPrichod);
    }

    @Override
    public synchronized void replication() {
        this.simulateEvents(0);
    }

    @Override
    public void afterReplication() {
        celkoveCasy[0] += casy[0] / statsVykonov[9];
        celkoveCasy[1] += casy[1] / statsVykonov[9];
        celkoveCasy[2] += casy[2] / (statsVykonov[0] + statsVykonov[4]);

        if (pracoviskoRecepcia.getLastRadChange() != 0)
            celkoveDlzkyRadov[0] += dlzkyRadov[0] / (getEndTime() + 1);
        if (pracoviskoUcesy.getLastRadChange() != 0)
            celkoveDlzkyRadov[1] += dlzkyRadov[1] / (getEndTime() + 1);
        if (pracoviskoLicenie.getLastRadChange() != 0)
            celkoveDlzkyRadov[2] += dlzkyRadov[2] / (getEndTime() + 1);

        for (int i = 0; i < statsVykonov.length; i++) {
            statsAllVykonov[i] += statsVykonov[i];
        }

        pocetReplikacii++;
    }

    @Override
    public void afterReplications() {
        for (Zamestnanec zamestnanec : zamestnanci) {
            zamestnanec.setVyuzitie(zamestnanec.getOdpracovanyCas() / this.getSimTime());
        }
        this.refreshGUI();
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

    public RandUniformDiscrete getRandUcesJednoduchy() {
        return randUcesJednoduchy;
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

    public RandExponential getRandPrichod() {
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

    public int getPocetObsluhovanychRecepcia() {
        return pocetObsluhovanychRecepcia;
    }

    public void incPocetObsluhovanychRecepcia(int dlzka) {
        this.pocetObsluhovanychRecepcia += dlzka;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getPocetReplikacii() {
        return pocetReplikacii;
    }

    public ArrayList<Zamestnanec> getZamestnanci() {
        return zamestnanci;
    }

    public ArrayList<ZakaznikSalonu> getZakaznici() {
        return zakaznici;
    }

    public String[] getStatsNames() {
        return statsNames;
    }

    public double[] getStatsAllVykonov() {
        return statsAllVykonov;
    }

    public int[] getStatsVykonov() {
        return statsVykonov;
    }

    public void addCas(int index, double cas) {
        casy[index] += cas;
    }

    public void addDlzkaRadu(int index, double dlzkaRadu) {
        dlzkyRadov[index] += dlzkaRadu;
    }

    public double[] getDlzkyRadov() {
        return dlzkyRadov;
    }

    public double[] getCelkoveDlzkyRadov() {
        return celkoveDlzkyRadov;
    }

    public double[] getCasy() {
        return casy;
    }

    public double[] getCelkoveCasy() {
        return celkoveCasy;
    }

    public double[] getChikvadrat() {
        return chikvadrat;
    }

    public int getN() {
        return n;
    }

    public void incN() {
        this.n++;
    }
}
