package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventPrichod extends Event {

    SalonSimulation salonSimulation;
    ZakaznikSalonu zakaznikSalonu;

    public EventPrichod(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore) {
        super(time, eventCore, "EventPrichod");
        salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
    }

    @Override
    public void vykonaj() {
//        System.out.println("Prichod: " + getTime());
//        System.out.println(zakaznikSalonu);
        if (salonSimulation.getRadRecepcia().isEmpty() && salonSimulation.getPracoviskoRecepcia().jeNiektoVolny() && salonSimulation.getDlzkaRaduUcesyLicenie() <= 10) {
            EventRecepciaStart zaciatokRecepcie = new EventRecepciaStart(this.zakaznikSalonu, this.getTime(), salonSimulation);
            salonSimulation.addToKalendar(zaciatokRecepcie);
        } else {
            salonSimulation.getRadRecepcia().add(this.zakaznikSalonu);
        }
        ZakaznikSalonu zakaznikSalonu = new ZakaznikSalonu(this.getTime() + salonSimulation.getRandPrichod().nextValue());
        if (zakaznikSalonu.getCasPrichodu() <= salonSimulation.getEndTime()) {
            EventPrichod prichod = new EventPrichod(zakaznikSalonu, zakaznikSalonu.getCasPrichodu(), salonSimulation);
            salonSimulation.addToKalendar(prichod);
        } else {
            EventZatvorenie zatvorenie = new EventZatvorenie(salonSimulation.getEndTime(), salonSimulation);
            salonSimulation.addToKalendar(zatvorenie);
        }
    }

}
