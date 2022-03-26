package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventUcesStart extends Event implements Comparable<Event>{

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;
    private final Zamestnanec zamestnanec;

    public EventUcesStart(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore, Zamestnanec zamestnanec) {
        super(time, eventCore, "UcesStart");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
        this.zamestnanec = zamestnanec;
    }

    @Override
    public void vykonaj() {
//        System.out.println(this);
        double percentage = salonSimulation.getRandPercentageTypUcesu().nextDouble();
        double endTime;
        if (percentage < 0.4) {
            endTime = salonSimulation.getRandUcetJednoduchy().nextValue();
        } else if (percentage < 0.8) {
            endTime = salonSimulation.getRandUcesZlozity().nextValue();
        } else {
            endTime = salonSimulation.getRandUcesSvadobny().nextValue();
        }
        EventUcesEnd eventUcesEnd = new EventUcesEnd(zakaznikSalonu, this.getTime() + endTime, salonSimulation, zamestnanec);
        zamestnanec.addOdpracovanyCas(endTime);
        salonSimulation.addToKalendar(eventUcesEnd);
    }


    @Override
    public int compareTo(Event o) {
        return this.getTime().compareTo(o.getTime());
    }
}
