package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.TypZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventUcesEnd extends Event {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;
    private final Zamestnanec zamestnanec;

    public EventUcesEnd(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore, Zamestnanec zamestnanec) {
        super(time, eventCore, "UcesEnd");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
        this.zamestnanec = zamestnanec;
    }

    @Override
    public void vykonaj() {
        //System.out.println(this);
        if (zakaznikSalonu.getTypZakaznika() == TypZakaznika.UCESAJLICENIE) {
            salonSimulation.statsVykonov[5]++;
        } else {
            salonSimulation.statsVykonov[1]++;
        }
        salonSimulation.getPracoviskoUcesy().uvolniZamestnanca(zamestnanec);
        if (zakaznikSalonu.getTypZakaznika() == TypZakaznika.UCESAJLICENIE) {
            EventMethod.obsluhaOrRad(salonSimulation, salonSimulation.getRadLicenie(), salonSimulation.getPracoviskoLicenie(), EventStartType.LICENIE, this.getTime(), zakaznikSalonu);
        } else {
            EventMethod.obsluhaOrRad(salonSimulation, salonSimulation.getRadRecepcia(), salonSimulation.getPracoviskoRecepcia(), EventStartType.RECEPCIA, this.getTime(), zakaznikSalonu);
        }
        if (EventMethod.planStart(salonSimulation, salonSimulation.getRadUces(), salonSimulation.getPracoviskoUcesy(), EventStartType.UCES, this.getTime())) {
            if ((this.getTime() <= salonSimulation.getEndTime() && salonSimulation.getDlzkaRaduUcesyLicenie() <= 10) || (!salonSimulation.getRadRecepcia().isEmpty() && salonSimulation.getRadRecepcia().peek().isObsluzeny())) {
                EventMethod.planStart(salonSimulation, salonSimulation.getRadRecepcia(), salonSimulation.getPracoviskoRecepcia(), EventStartType.RECEPCIA, this.getTime());
            }
        }

    }

}
