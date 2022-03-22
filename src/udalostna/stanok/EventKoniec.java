package udalostna.stanok;

import simCores.EventCore;
import udalostna.Event;

public class EventKoniec extends Event {

    StanokSimulation stanokSimulation;
    Zakaznik zakaznik;

    public EventKoniec(Zakaznik zakaznik, double time, EventCore eventCore) {
        super(time, eventCore);
        stanokSimulation = (StanokSimulation) eventCore;
        this.zakaznik = zakaznik;
    }

    @Override
    public void vykonaj() {
//        System.out.println("Koniec: " + time);
//        System.out.println(zakaznik);
        if (!stanokSimulation.rad.isEmpty()) {
            EventZaciatok zaciatok = new EventZaciatok(stanokSimulation.rad.poll(), this.getTime(), stanokSimulation);
            stanokSimulation.dlzkaRadu += zakaznik.getCasCakania() * stanokSimulation.rad.size();
            stanokSimulation.addToKalendar(zaciatok);
            stanokSimulation.obsluhaPrebieha = true;
        } else {
            stanokSimulation.obsluhaPrebieha = false;
        }

    }


    @Override
    public int compareTo(Event o) {
        return this.getTime().compareTo(o.getTime());
    }
}
