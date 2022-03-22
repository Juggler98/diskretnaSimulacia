package udalostna.stanok;

import simCores.EventCore;
import udalostna.Event;

public class EventZaciatok extends Event {

    StanokSimulation stanokSimulation;
    Zakaznik zakaznik;

    public EventZaciatok(Zakaznik zakaznik, double time, EventCore eventCore) {
        super(time, eventCore);
        this.zakaznik = zakaznik;
        stanokSimulation = (StanokSimulation) eventCore;
    }

    @Override
    public void vykonaj() {
//        System.out.println("Zaciatok: " + time);
//        System.out.println(zakaznik);
        EventKoniec koniec = new EventKoniec(this.zakaznik, this.getTime() + stanokSimulation.randObsluha.getNextDouble(), stanokSimulation);
        stanokSimulation.addToKalendar(koniec);
        zakaznik.setCasCakania(this.getTime() - zakaznik.getCasPrichodu());
        stanokSimulation.dlzkaCakania += zakaznik.getCasCakania();
        stanokSimulation.pocetObsluzenych++;
        //System.out.println(coreStanok.dlzkaCakania / coreStanok.pocetObsluzenych);
        //System.out.println("Pocet zakaznikov: " + coreStanok.pocetObsluzenych);
    }


    @Override
    public int compareTo(Event o) {
        return this.getTime().compareTo(o.getTime());
    }
}
