package udalostna.stanok;

import simCores.EventCore;
import udalostna.Event;

public class EventKoniec extends Event {

    StanokSimulation stanokSimulation;
    ZakaznikStanku zakaznikStanku;

    public EventKoniec(ZakaznikStanku zakaznikStanku, double time, EventCore eventCore) {
        super(time, eventCore, "EventKoniec");
        stanokSimulation = (StanokSimulation) eventCore;
        this.zakaznikStanku = zakaznikStanku;
    }

    @Override
    public void vykonaj() {
//        System.out.println("Koniec: " + time);
//        System.out.println(zakaznik);
        if (!stanokSimulation.rad.isEmpty()) {
            EventZaciatok zaciatok = new EventZaciatok(stanokSimulation.rad.poll(), this.getTime(), stanokSimulation);

            stanokSimulation.dlzkaRadu += (getTime() - stanokSimulation.poslednaZmenaRadu) * (stanokSimulation.rad.size() + 1);
            stanokSimulation.poslednaZmenaRadu = getTime();

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
