package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.zakaznik.StavZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventPrichod extends Event {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;

    public EventPrichod(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore) {
        super(time, eventCore, "EventPrichod");
        salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
    }

    @Override
    public void vykonaj() {
        if (salonSimulation.getRadRecepcia().isEmpty() && salonSimulation.getPracoviskoRecepcia().jeNiektoVolny() && salonSimulation.getDlzkaRaduUcesyLicenie() <= 10) {
            EventRecepciaStart zaciatokRecepcie = new EventRecepciaStart(this.zakaznikSalonu, this.getTime(), salonSimulation);
            salonSimulation.addToKalendar(zaciatokRecepcie);
        } else {
            salonSimulation.getRadRecepcia().add(this.zakaznikSalonu);
            zakaznikSalonu.setStavZakaznika(StavZakaznika.RADRECEPCIA);
        }
        ZakaznikSalonu zakaznikSalonu = new ZakaznikSalonu(this.getTime() + salonSimulation.getRandPrichod().nextValue());
        if (zakaznikSalonu.getCasPrichodu() <= salonSimulation.getEndTime()) {
            zakaznikSalonu.setStavZakaznika(StavZakaznika.PRICHOD);
            salonSimulation.getZakaznici().add(zakaznikSalonu);
            EventPrichod prichod = new EventPrichod(zakaznikSalonu, zakaznikSalonu.getCasPrichodu(), salonSimulation);
            salonSimulation.addToKalendar(prichod);
        } else {
            EventZatvorenie zatvorenie = new EventZatvorenie(salonSimulation.getEndTime(), salonSimulation);
            salonSimulation.addToKalendar(zatvorenie);
        }
    }

}
