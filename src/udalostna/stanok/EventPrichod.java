package udalostna.stanok;

import simCores.EventCore;
import udalostna.Event;

public class EventPrichod extends Event implements Comparable<Event> {

    StanokSimulation stanokSimulation;
    Zakaznik zakaznik;

    public EventPrichod(Zakaznik zakaznik, double time, EventCore eventCore) {
        super(time, eventCore);
        stanokSimulation = (StanokSimulation) eventCore;
        this.zakaznik = zakaznik;
    }

    @Override
    public void vykonaj() {
//        System.out.println("Prichod: " + time);
//        System.out.println(zakaznik);
        if (stanokSimulation.obsluhaPrebieha) {
            stanokSimulation.rad.add(zakaznik);
        } else {
            EventZaciatok zaciatok = new EventZaciatok(zakaznik, this.getTime(), stanokSimulation);
            stanokSimulation.addToKalendar(zaciatok);
            stanokSimulation.obsluhaPrebieha = true;
        }
        Zakaznik zakaznik = new Zakaznik(this.getTime() + stanokSimulation.randPrichod.getNextDouble());
        EventPrichod prichod = new EventPrichod(zakaznik, zakaznik.getCasPrichodu(), stanokSimulation);
        stanokSimulation.addToKalendar(prichod);
    }


    @Override
    public int compareTo(Event o) {
        return this.getTime().compareTo(o.getTime());
    }
}
