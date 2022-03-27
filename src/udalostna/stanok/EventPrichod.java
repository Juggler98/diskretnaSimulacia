package udalostna.stanok;

import simCores.EventCore;
import udalostna.Event;

public class EventPrichod extends Event implements Comparable<Event> {

    StanokSimulation stanokSimulation;
    ZakaznikStanku zakaznikStanku;

    public EventPrichod(ZakaznikStanku zakaznikStanku, double time, EventCore eventCore) {
        super(time, eventCore, "EventPrichod");
        stanokSimulation = (StanokSimulation) eventCore;
        this.zakaznikStanku = zakaznikStanku;
    }

    @Override
    public void vykonaj() {
//        System.out.println("Prichod: " + time);
//        System.out.println(zakaznik);
        if (stanokSimulation.obsluhaPrebieha) {
            stanokSimulation.rad.add(this.zakaznikStanku);
        } else {
            EventZaciatok zaciatok = new EventZaciatok(this.zakaznikStanku, this.getTime(), stanokSimulation);
            stanokSimulation.addToKalendar(zaciatok);
            stanokSimulation.obsluhaPrebieha = true;
        }
        ZakaznikStanku zakaznikStanku = new ZakaznikStanku(this.getTime() + stanokSimulation.randPrichod.nextValue());
        EventPrichod prichod = new EventPrichod(zakaznikStanku, zakaznikStanku.getCasPrichodu(), stanokSimulation);
        stanokSimulation.addToKalendar(prichod);
    }


    @Override
    public int compareTo(Event o) {
        return this.getTime().compareTo(o.getTime());
    }
}
