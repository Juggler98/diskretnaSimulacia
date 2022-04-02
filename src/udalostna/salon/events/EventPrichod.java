package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
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
        if (salonSimulation.getDlzkaRaduUcesyLicenie() + salonSimulation.getPocetObsluhovanychRecepcia() <= 10) {
            EventMethod.obsluhaOrRad(salonSimulation, salonSimulation.getRadRecepcia(), salonSimulation.getPracoviskoRecepcia(), EventStartType.RECEPCIA, this.getTime(), zakaznikSalonu);
        } else {
            salonSimulation.addDlzkaRadu(0, salonSimulation.getRadRecepcia().size() * (getTime() - salonSimulation.getPracoviskoRecepcia().getLastRadChange()));
            salonSimulation.getPracoviskoRecepcia().setLastRadChange(getTime());
            salonSimulation.getRadRecepcia().add(this.zakaznikSalonu);
            zakaznikSalonu.setStavZakaznika(StavZakaznika.RADRECEPCIA);
        }
        ZakaznikSalonu zakaznikSalonu = new ZakaznikSalonu(this.getTime() + salonSimulation.getRandPrichod().nextValue(), salonSimulation.getZakaznici().size() + 1);
        if (zakaznikSalonu.getCasPrichodu() <= salonSimulation.getEndTime()) {
            zakaznikSalonu.setStavZakaznika(StavZakaznika.PRICHOD);
            salonSimulation.getZakaznici().add(zakaznikSalonu);
            EventPrichod prichod = new EventPrichod(zakaznikSalonu, zakaznikSalonu.getCasPrichodu(), salonSimulation);
            salonSimulation.addToKalendar(prichod);
        }
    }

}
