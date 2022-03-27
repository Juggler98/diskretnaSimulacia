package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.StavZakaznika;
import udalostna.salon.zakaznik.TypZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

public class EventRecepciaEnd extends Event {

    private final SalonSimulation salonSimulation;
    private final ZakaznikSalonu zakaznikSalonu;
    private final Zamestnanec zamestnanec;

    public EventRecepciaEnd(ZakaznikSalonu zakaznikSalonu, double time, EventCore eventCore, Zamestnanec zamestnanec) {
        super(time, eventCore, "RecepciaEnd");
        this.salonSimulation = (SalonSimulation) eventCore;
        this.zakaznikSalonu = zakaznikSalonu;
        this.zamestnanec = zamestnanec;
    }

    @Override
    public void vykonaj() {
        if (!this.zakaznikSalonu.isObsluzeny()) {
            double percentage = salonSimulation.getRandPercentageTypZakaznika().nextDouble();
            if (percentage < 0.2) {
                zakaznikSalonu.setTypZakaznika(TypZakaznika.UCES);
                salonSimulation.getStatsVykonov()[0]++;
            } else if (percentage < 0.35) {
                salonSimulation.getStatsVykonov()[2]++;
                zakaznikSalonu.setTypZakaznika(TypZakaznika.LICENIE);
            } else {
                salonSimulation.getStatsVykonov()[4]++;
                zakaznikSalonu.setTypZakaznika(TypZakaznika.UCESAJLICENIE);
            }
            if (zakaznikSalonu.getTypZakaznika() == TypZakaznika.LICENIE || zakaznikSalonu.getTypZakaznika() == TypZakaznika.UCESAJLICENIE) {
                double percentage2 = salonSimulation.getRandPercentageCiseniePleti().nextDouble();
                if (percentage2 < 0.35) {
                    salonSimulation.getStatsVykonov()[6]++;
                    zakaznikSalonu.setGoToHlbkoveLicenie(true);
                    zakaznikSalonu.setHlbkoveLicenie(true);
                }
            }
            if (zakaznikSalonu.getTypZakaznika() == TypZakaznika.LICENIE) {
                EventMethod.obsluhaOrRad(salonSimulation, salonSimulation.getRadLicenie(), salonSimulation.getPracoviskoLicenie(), EventStartType.LICENIE, this.getTime(), zakaznikSalonu);
            } else {
                EventMethod.obsluhaOrRad(salonSimulation, salonSimulation.getRadUces(), salonSimulation.getPracoviskoUcesy(), EventStartType.UCES, this.getTime(), zakaznikSalonu);
            }
            zakaznikSalonu.setObsluzeny();
        } else {
            zakaznikSalonu.setCasOdchodu(this.getTime());
            zakaznikSalonu.setStavZakaznika(StavZakaznika.ODCHOD);
            salonSimulation.addCasStravenyVSalone(zakaznikSalonu.getCasOdchodu() - zakaznikSalonu.getCasPrichodu());
            salonSimulation.getStatsVykonov()[9]++;
        }
        zamestnanec.setObsluhuje(false);
        zamestnanec.addOdpracovanyCas(this.getTime() - zamestnanec.getZaciatokObsluhy());
        zamestnanec.setVyuzitie(zamestnanec.getOdpracovanyCas() / salonSimulation.getSimTime());
        salonSimulation.getPracoviskoRecepcia().uvolniZamestnanca(zamestnanec);
        if ((this.getTime() <= salonSimulation.getEndTime() && salonSimulation.getDlzkaRaduUcesyLicenie() <= 10) || (!salonSimulation.getRadRecepcia().isEmpty() && salonSimulation.getRadRecepcia().peek().isObsluzeny())) {
            EventMethod.planStart(salonSimulation, salonSimulation.getRadRecepcia(), salonSimulation.getPracoviskoRecepcia(), EventStartType.RECEPCIA, this.getTime());
        }
    }

}
