package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventLicenieStart extends Event {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;
    private final Zamestnanec zamestnanec;

    public EventLicenieStart(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore, Zamestnanec zamestnanec) {
        super(time, eventCore, "LicenieStart");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
        this.zamestnanec = zamestnanec;
    }

    @Override
    public void vykonaj() {
        double endTime;
        if (zakaznikSalonu.isHlbkoveLicenie()) {
            endTime = salonSimulation.getRandHlbkoveCistenie().nextValue();
        } else {
            double percentage = salonSimulation.getRandPercentageTypLicenia().nextDouble();
            if (percentage < 0.3) {
                endTime = salonSimulation.getRandLicenieJednoduche().nextValue();
            } else {
                endTime = salonSimulation.getRandLicenieZlozite().nextValue();
            }
        }
        EventLicenieEnd eventLicenieEnd = new EventLicenieEnd(zakaznikSalonu, this.getTime() + endTime, salonSimulation, zamestnanec);
        zamestnanec.addOdpracovanyCas(endTime);
        salonSimulation.addToKalendar(eventLicenieEnd);
    }


}
