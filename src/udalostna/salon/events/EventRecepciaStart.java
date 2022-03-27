package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventRecepciaStart extends Event {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;

    public EventRecepciaStart(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore) {
        super(time, eventCore, "RecepciaStart");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
    }

    @Override
    public void vykonaj() {
        double koniecRecepcieTime;
//        System.out.println(this.getTime() / 3600);
        if (salonSimulation.getPracoviskoRecepcia().jeNiektoVolny()) {
            if (this.zakaznikSalonu.isObsluzeny()) {
                koniecRecepcieTime = salonSimulation.getRandPlatba().nextValue();
            } else {
                salonSimulation.statsVykonov[8]++;
                koniecRecepcieTime = salonSimulation.getRandObjednavka().nextValue();
                zakaznikSalonu.setCasCakania(this.getTime() - zakaznikSalonu.getCasPrichodu());
                salonSimulation.dlzkaCakania += zakaznikSalonu.getCasCakania();
            }
            Zamestnanec zamestnanec = salonSimulation.getPracoviskoRecepcia().obsadZamestnanca();
            zamestnanec.setObsluhuje(true);
            EventRecepciaEnd koniecRecepcie = new EventRecepciaEnd(this.zakaznikSalonu,this.getTime() + koniecRecepcieTime, salonSimulation, zamestnanec);
            zamestnanec.setZaciatokObsluhy(this.getTime());
            salonSimulation.addToKalendar(koniecRecepcie);
        } else {
            salonSimulation.getRadRecepcia().add(zakaznikSalonu);
        }
    }

}
