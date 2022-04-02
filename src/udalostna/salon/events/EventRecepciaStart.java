package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.StavZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventRecepciaStart extends Event {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;
    private final Zamestnanec zamestnanec;

    public EventRecepciaStart(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore, Zamestnanec zamestnanec) {
        super(time, eventCore, "RecepciaStart");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
        this.zamestnanec = zamestnanec;
    }

    @Override
    public void vykonaj() {
        double koniecRecepcieTime;
        if (this.zakaznikSalonu.isObsluzeny()) {
            koniecRecepcieTime = salonSimulation.getRandPlatba().nextValue();
            zakaznikSalonu.setCasZaciatkuObsluhy(4, this.getTime());
            zakaznikSalonu.setStavZakaznika(StavZakaznika.PLATBA);
        } else {
            salonSimulation.incPocetObsluhovanychRecepcia(1);
            salonSimulation.getStatsVykonov()[8]++;
            koniecRecepcieTime = salonSimulation.getRandObjednavka().nextValue();

            zakaznikSalonu.setCasZaciatkuObsluhy(0, this.getTime());
            zakaznikSalonu.setStavZakaznika(StavZakaznika.OBJEDNAVKA);
            salonSimulation.addCas(1, this.getTime() - zakaznikSalonu.getCasPrichodu());
        }
        zamestnanec.setObsluhuje(true);
        zamestnanec.setObsluhujeZakaznika(zakaznikSalonu.getPoradie());
        EventRecepciaEnd koniecRecepcie = new EventRecepciaEnd(this.zakaznikSalonu, this.getTime() + koniecRecepcieTime, salonSimulation, zamestnanec);
        zamestnanec.setZaciatokObsluhy(this.getTime());
        salonSimulation.addToKalendar(koniecRecepcie);
    }

}
