package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;

public class EventZatvorenie extends Event {

    SalonSimulation salonSimulation;

    public EventZatvorenie(double time, EventCore eventCore) {
        super(time, eventCore, "EventZatvorenie");
        salonSimulation = (SalonSimulation) eventCore;
    }

    @Override
    public void vykonaj() {
        salonSimulation.getRadRecepcia().removeIf(z -> !z.isObsluzeny());
    }
}
