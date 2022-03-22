package udalostna.stanok;

import generators.RandExponential;
import generators.RandUniformContinuous;
import simCores.EventCore;

import java.util.LinkedList;
import java.util.Queue;

public class StanokSimulation extends EventCore {

    protected Queue<Zakaznik> rad = new LinkedList<>();
    protected boolean obsluhaPrebieha = false;
    protected RandExponential randPrichod = new RandExponential(240);
    protected RandUniformContinuous randObsluha = new RandUniformContinuous(120, 240);

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

        Zakaznik zakaznik = new Zakaznik(randPrichod.getNextDouble());
        EventPrichod prichod = new EventPrichod(zakaznik, zakaznik.getCasPrichodu(), this);
        kalendarUdalosti.add(prichod);
        if (obsluhaPrebieha) {
            rad.add(zakaznik);
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
