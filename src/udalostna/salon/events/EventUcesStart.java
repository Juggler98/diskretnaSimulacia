package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.StavZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventUcesStart extends Event {

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
        double percentage = salonSimulation.getRandPercentageTypUcesu().nextDouble();
        double endTime;
        if (percentage < 0.4) {
            endTime = salonSimulation.getRandUcesJednoduchy().nextValue();
        } else if (percentage < 0.8) {
            endTime = salonSimulation.getRandUcesZlozity().nextValue();
        } else {
            endTime = salonSimulation.getRandUcesSvadobny().nextValue();
        }
        endTime *= 60;
        zakaznikSalonu.setStavZakaznika(StavZakaznika.UCES);
        zakaznikSalonu.setCasZaciatkuObsluhy(1, this.getTime());
        zamestnanec.setObsluhuje(true);
        zamestnanec.setObsluhujeZakaznika(zakaznikSalonu.getPoradie());
        zamestnanec.setZaciatokObsluhy(this.getTime());
        salonSimulation.addCas(2, endTime);
        EventUcesEnd eventUcesEnd = new EventUcesEnd(zakaznikSalonu, this.getTime() + endTime, salonSimulation, zamestnanec);
        salonSimulation.addToKalendar(eventUcesEnd);
    }

}
