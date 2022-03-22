package udalostna.stanok;

import generators.RandExponential;
import generators.RandUniformContinuous;
import simCores.EventCore;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class StanokSimulation extends EventCore {

    protected Queue<ZakaznikStanku> rad = new LinkedList<>();
    protected boolean obsluhaPrebieha = false;
    private final Random seedGenerator = new Random();
    protected RandExponential randPrichod = new RandExponential(240, seedGenerator);
    protected RandUniformContinuous randObsluha = new RandUniformContinuous(120, 240, seedGenerator);

    protected double dlzkaCakania = 0;
    protected double dlzkaRadu = 0;
    protected int pocetObsluzenych = 0;

    private double celkovaDlzkaCakania = 0;
    private double celkovaDlzkaRadu = 0;
    private int pocetReplikacii = 0;

    private int endTime;

    public StanokSimulation(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public void beforeReplications() {

    }

    @Override
    public void beforeReplication() {
        dlzkaCakania = 0;
        dlzkaRadu = 0;
        pocetObsluzenych = 0;

        rad.clear();
        kalendarUdalosti.clear();
        obsluhaPrebieha = false;

        ZakaznikStanku zakaznikStanku = new ZakaznikStanku(randPrichod.nextValue());
        EventPrichod prichod = new EventPrichod(zakaznikStanku, zakaznikStanku.getCasPrichodu(), this);
        kalendarUdalosti.add(prichod);
        if (obsluhaPrebieha) {
            rad.add(zakaznikStanku);
        }
    }

    @Override
    public void replication() {
        this.simulateEvents(endTime);
    }

    @Override
    public void afterReplication() {
        celkovaDlzkaCakania += dlzkaCakania / pocetObsluzenych;
        celkovaDlzkaRadu += dlzkaRadu / dlzkaCakania;
        pocetReplikacii++;
    }

    @Override
    public void afterReplications() {
        System.out.println("PriemernaDlzkaCakania: " + celkovaDlzkaCakania / pocetReplikacii);
        System.out.println("PriemernaDlzkaRadu: " + celkovaDlzkaRadu / pocetReplikacii);
        System.out.println("Pocet replikacii: " + pocetReplikacii);
        System.out.println("Dlzka replikacie: " + endTime);

    }


}
