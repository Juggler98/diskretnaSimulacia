package udalostna.salon.events;

import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Pracovisko;
import udalostna.salon.zakaznik.StavZakaznika;
import udalostna.salon.zakaznik.ZakaznikSalonu;

import java.util.Queue;

public abstract class EventMethod {

    public static boolean planStart(SalonSimulation salonSimulation, Queue<ZakaznikSalonu> rad, Pracovisko pracovisko, EventStartType eventStartType, double time) {
        if (!rad.isEmpty() && pracovisko.jeNiektoVolny()) {
            ZakaznikSalonu zakaznikSalonu = rad.poll();
            Event event = null;
            int radIndex = 0;
            switch (eventStartType) {
                case UCES:
                    radIndex = 1;
                    event = new EventUcesStart(zakaznikSalonu, time, salonSimulation, pracovisko.obsadZamestnanca());
                    break;
                case LICENIE:
                    radIndex = 2;
                    event = new EventLicenieStart(zakaznikSalonu, time, salonSimulation, pracovisko.obsadZamestnanca());
                    break;
                case RECEPCIA:
                    radIndex = 0;
                    event = new EventRecepciaStart(zakaznikSalonu, time, salonSimulation, pracovisko.obsadZamestnanca());
                    break;
            }
            salonSimulation.addDlzkaRadu(radIndex,(rad.size() + 1) * (time - pracovisko.getLastRadChange()));
            pracovisko.setLastRadChange(time);
            salonSimulation.addToKalendar(event);
            return true;
        }
        return false;
    }

    public static void obsluhaOrRad(SalonSimulation salonSimulation, Queue<ZakaznikSalonu> rad, Pracovisko pracovisko, EventStartType eventStartType, double time, ZakaznikSalonu zakaznikSalonu) {
        if (rad.isEmpty() && pracovisko.jeNiektoVolny()) {
            Event event = null;
            switch (eventStartType) {
                case UCES:
                    event = new EventUcesStart(zakaznikSalonu, time, salonSimulation, pracovisko.obsadZamestnanca());
                    break;
                case LICENIE:
                    event = new EventLicenieStart(zakaznikSalonu, time, salonSimulation, pracovisko.obsadZamestnanca());
                    break;
                case RECEPCIA:
                    event = new EventRecepciaStart(zakaznikSalonu, time, salonSimulation, pracovisko.obsadZamestnanca());
                    break;
            }
            salonSimulation.addToKalendar(event);
        } else {
            int radIndex = 0;
            switch (eventStartType) {
                case UCES:
                    radIndex = 1;
                    zakaznikSalonu.setStavZakaznika(StavZakaznika.RADUCES);
                    break;
                case LICENIE:
                    radIndex = 2;
                    zakaznikSalonu.setStavZakaznika(StavZakaznika.RADLICENIE);
                    break;
                case RECEPCIA:
                    radIndex = 0;
                    zakaznikSalonu.setStavZakaznika(StavZakaznika.RADRECEPCIA);
                    break;
            }
            salonSimulation.addDlzkaRadu(radIndex, rad.size() * (time - pracovisko.getLastRadChange()));
            pracovisko.setLastRadChange(time);
            rad.add(zakaznikSalonu);
        }
    }


}
