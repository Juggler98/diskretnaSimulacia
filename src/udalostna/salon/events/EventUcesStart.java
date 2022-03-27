package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventUcesStart extends Event {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;

    public EventUcesStart(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore) {
        super(time, eventCore, "UcesStart");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
    }

    @Override
    public void vykonaj() {
        //System.out.println(this);
        if (salonSimulation.getPracoviskoUcesy().jeNiektoVolny()) {
            double percentage = salonSimulation.getRandPercentageTypUcesu().nextDouble();
            double endTime;
            if (percentage < 0.4) {
                endTime = salonSimulation.getRandUcesJednoduchy().nextValue();
            } else if (percentage < 0.8) {
                endTime = salonSimulation.getRandUcesZlozity().nextValue();
            } else {
                endTime = salonSimulation.getRandUcesSvadobny().nextValue();
            }
            Zamestnanec zamestnanec = salonSimulation.getPracoviskoUcesy().obsadZamestnanca();
            zamestnanec.setObsluhuje(true);
            zamestnanec.setZaciatokObsluhy(this.getTime());
            EventUcesEnd eventUcesEnd = new EventUcesEnd(zakaznikSalonu, this.getTime() + endTime, salonSimulation, zamestnanec);
            salonSimulation.addToKalendar(eventUcesEnd);
        } else {
            salonSimulation.getRadUces().add(zakaznikSalonu);
        }
    }

}
