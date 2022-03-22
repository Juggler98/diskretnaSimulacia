package udalostna.stanok;

import simCores.EventCore;
import udalostna.Event;

public class EventZaciatok extends Event {

    StanokSimulation stanokSimulation;
    ZakaznikStanku zakaznikStanku;

    public EventZaciatok(ZakaznikStanku zakaznikStanku, double time, EventCore eventCore) {
        super(time, eventCore);
        this.zakaznikStanku = zakaznikStanku;
        stanokSimulation = (StanokSimulation) eventCore;
    }

    @Override
    public void vykonaj() {
//        System.out.println("Zaciatok: " + time);
//        System.out.println(zakaznik);
        EventKoniec koniec = new EventKoniec(this.zakaznikStanku, this.getTime() + stanokSimulation.randObsluha.nextValue(), stanokSimulation);
        stanokSimulation.addToKalendar(koniec);
        zakaznikStanku.setCasCakania(this.getTime() - zakaznikStanku.getCasPrichodu());
        stanokSimulation.dlzkaCakania += zakaznikStanku.getCasCakania();
        stanokSimulation.pocetObsluzenych++;
        //System.out.println(coreStanok.dlzkaCakania / coreStanok.pocetObsluzenych);
        //System.out.println("Pocet zakaznikov: " + coreStanok.pocetObsluzenych);
    }


    @Override
    public int compareTo(Event o) {
        return this.getTime().compareTo(o.getTime());
    }
}
