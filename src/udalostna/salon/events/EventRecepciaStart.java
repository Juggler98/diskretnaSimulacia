package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventRecepciaStart extends Event {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;
    private final Zamestnanec zamestnanec;

    public EventRecepciaStart(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore, Zamestnanec zamestnanec) {
        super(time, eventCore, "RecepciaEnd");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
        this.zamestnanec = zamestnanec;
    }

    @Override
    public void vykonaj() {
        double koniecRecepcieTime;
//        System.out.println(this.getTime() / 3600);
        if (this.zakaznikSalonu.isObsluzeny()) {
            koniecRecepcieTime = salonSimulation.getRandPlatba().nextValue();
        } else {
            salonSimulation.statsVykonov[8]++;
            koniecRecepcieTime = salonSimulation.getRandObjednavka().nextValue();
            zakaznikSalonu.setCasCakania(this.getTime() - zakaznikSalonu.getCasPrichodu());
            salonSimulation.dlzkaCakania += zakaznikSalonu.getCasCakania();
        }
        EventRecepciaEnd koniecRecepcie = new EventRecepciaEnd(this.zakaznikSalonu,this.getTime() + koniecRecepcieTime, salonSimulation, zamestnanec);
        zamestnanec.addOdpracovanyCas(koniecRecepcieTime);
        salonSimulation.addToKalendar(koniecRecepcie);
    }

}
