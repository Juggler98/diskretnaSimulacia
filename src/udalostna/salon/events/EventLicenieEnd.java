package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.TypZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventLicenieEnd extends Event implements Comparable<Event> {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;
    private final Zamestnanec zamestnanec;

    public EventLicenieEnd(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore, Zamestnanec zamestnanec) {
        super(time, eventCore, "LicenieEnd");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
        this.zamestnanec = zamestnanec;
    }

    @Override
    public void vykonaj() {
        if (zakaznikSalonu.getTypZakaznika() != TypZakaznika.UCESAJLICENIE && !zakaznikSalonu.isHlbkoveLicenie()) {
            salonSimulation.statsVykonov[3]++;
        }
        if (zakaznikSalonu.isHlbkoveLicenie())
            salonSimulation.statsVykonov[7]++;
        salonSimulation.getPracoviskoLicenie().uvolniZamestnanca(zamestnanec);
        if (zakaznikSalonu.isHlbkoveLicenie()) {
            if (salonSimulation.getRadLicenie().isEmpty() && salonSimulation.getPracoviskoLicenie().jeNiektoVolny()) {
                Zamestnanec zamestnanec = salonSimulation.getPracoviskoLicenie().obsadZamestnanca();
                EventLicenieStart eventLicenieStart = new EventLicenieStart(zakaznikSalonu, this.getTime(), salonSimulation, zamestnanec);
                salonSimulation.addToKalendar(eventLicenieStart);
            } else {
                salonSimulation.getRadLicenie().add(zakaznikSalonu);
            }
            zakaznikSalonu.setHlbkoveLicenie(false);
        } else {
            if (salonSimulation.getRadRecepcia().isEmpty() && salonSimulation.getPracoviskoRecepcia().jeNiektoVolny()) {
                Zamestnanec zamestnanec = salonSimulation.getPracoviskoRecepcia().obsadZamestnanca();
                EventRecepciaStart zaciatokRecepcie = new EventRecepciaStart(this.zakaznikSalonu, this.getTime(), salonSimulation, zamestnanec);
                salonSimulation.addToKalendar(zaciatokRecepcie);
            } else {
                salonSimulation.getRadRecepcia().add(this.zakaznikSalonu);
            }
        }
        if (EventMethod.planStart(salonSimulation, salonSimulation.getRadLicenie(), salonSimulation.getPracoviskoLicenie(), EventStartType.LICENIE, this.getTime())) {
            if ((this.getTime() <= salonSimulation.getEndTime() && salonSimulation.getDlzkaRaduUcesyLicenie() <= 10) || (!salonSimulation.getRadRecepcia().isEmpty() && salonSimulation.getRadRecepcia().peek().isObsluzeny())) {
                EventMethod.planStart(salonSimulation, salonSimulation.getRadRecepcia(), salonSimulation.getPracoviskoRecepcia(), EventStartType.RECEPCIA, this.getTime());
            }
        }
    }


    @Override
    public int compareTo(Event o) {
        return this.getTime().compareTo(o.getTime());
    }
}
