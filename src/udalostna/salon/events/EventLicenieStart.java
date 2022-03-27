package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.StavZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventLicenieStart extends Event {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;

    public EventLicenieStart(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore) {
        super(time, eventCore, "LicenieStart");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
    }

    @Override
    public void vykonaj() {
        if (salonSimulation.getPracoviskoLicenie().jeNiektoVolny()) {
            double endTime;
            if (zakaznikSalonu.isGoToHlbkoveLicenie()) {
                endTime = salonSimulation.getRandHlbkoveCistenie().nextValue();
                zakaznikSalonu.setStavZakaznika(StavZakaznika.HLBKOVECISTENIE);
                zakaznikSalonu.setCasZaciatkuObsluhy(2, this.getTime());
            } else {
                zakaznikSalonu.setStavZakaznika(StavZakaznika.LICENIE);
                zakaznikSalonu.setCasZaciatkuObsluhy(3, this.getTime());
                double percentage = salonSimulation.getRandPercentageTypLicenia().nextDouble();
                if (percentage < 0.3) {
                    endTime = salonSimulation.getRandLicenieJednoduche().nextValue();
                } else {
                    endTime = salonSimulation.getRandLicenieZlozite().nextValue();
                }
            }
            Zamestnanec zamestnanec = salonSimulation.getPracoviskoLicenie().obsadZamestnanca();
            zamestnanec.setObsluhuje(true);
            zamestnanec.setZaciatokObsluhy(this.getTime());
            EventLicenieEnd eventLicenieEnd = new EventLicenieEnd(zakaznikSalonu, this.getTime() + endTime, salonSimulation, zamestnanec);
            salonSimulation.addToKalendar(eventLicenieEnd);
        } else {
            salonSimulation.getRadLicenie().add(zakaznikSalonu);
            zakaznikSalonu.setStavZakaznika(StavZakaznika.RADLICENIE);
        }

    }


}
