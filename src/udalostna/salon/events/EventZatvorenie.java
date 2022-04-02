package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.zakaznik.StavZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventZatvorenie extends Event {

    private final SalonSimulation salonSimulation;

    public EventZatvorenie(double time, EventCore eventCore) {
        super(time, eventCore, "EventZatvorenie");
        salonSimulation = (SalonSimulation) eventCore;
    }

    @Override
    public void vykonaj() {
        for (ZakaznikSalonu z : salonSimulation.getRadRecepcia()) {
            if (!z.isObsluzeny()) {
                z.setStavZakaznika(StavZakaznika.ODCHOD);
                z.setCasOdchodu(this.getTime());
            }
        }
        salonSimulation.addDlzkaRadu(0,salonSimulation.getRadRecepcia().size() * (getTime() - salonSimulation.getPracoviskoRecepcia().getLastRadChange()));
        salonSimulation.getPracoviskoRecepcia().setLastRadChange(getTime());
        salonSimulation.getRadRecepcia().removeIf(z -> !z.isObsluzeny());
    }
}
