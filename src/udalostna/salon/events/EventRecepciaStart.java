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

    public EventRecepciaStart(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore) {
        super(time, eventCore, "RecepciaStart");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
    }

    @Override
    public void vykonaj() {
        double koniecRecepcieTime;
        if (salonSimulation.getPracoviskoRecepcia().jeNiektoVolny()) {
            if (this.zakaznikSalonu.isObsluzeny()) {
                koniecRecepcieTime = salonSimulation.getRandPlatba().nextValue();
                zakaznikSalonu.setCasZaciatkuObsluhy(4, this.getTime());
                zakaznikSalonu.setStavZakaznika(StavZakaznika.PLATBA);
            } else {
                salonSimulation.getStatsVykonov()[8]++;
                koniecRecepcieTime = salonSimulation.getRandObjednavka().nextValue();

                zakaznikSalonu.setCasZaciatkuObsluhy(0, this.getTime());
                zakaznikSalonu.setStavZakaznika(StavZakaznika.OBJEDNAVKA);
                salonSimulation.addDlzkaCakaniaRecepcia(this.getTime() - zakaznikSalonu.getCasPrichodu());
            }
            Zamestnanec zamestnanec = salonSimulation.getPracoviskoRecepcia().obsadZamestnanca();
            zamestnanec.setObsluhuje(true);
            EventRecepciaEnd koniecRecepcie = new EventRecepciaEnd(this.zakaznikSalonu,this.getTime() + koniecRecepcieTime, salonSimulation, zamestnanec);
            zamestnanec.setZaciatokObsluhy(this.getTime());
            salonSimulation.addToKalendar(koniecRecepcie);
        } else {
            salonSimulation.getRadRecepcia().add(zakaznikSalonu);
            zakaznikSalonu.setStavZakaznika(StavZakaznika.RADRECEPCIA);
        }
    }

}
