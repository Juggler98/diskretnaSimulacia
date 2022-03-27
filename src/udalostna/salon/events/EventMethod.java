package udalostna.salon.events;

import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Pracovisko;
import udalostna.salon.zakaznik.ZakaznikSalonu;

import java.util.Queue;

public abstract class EventMethod {

    public static boolean planStart(SalonSimulation salonSimulation, Queue<ZakaznikSalonu> rad, Pracovisko pracovisko, EventStartType eventStartType, double time) {
        if (!rad.isEmpty() && pracovisko.jeNiektoVolny()) {
            ZakaznikSalonu zakaznikSalonu = rad.poll();
            Event event = null;
            switch (eventStartType) {
                case UCES:
                    event = new EventUcesStart(zakaznikSalonu, time, salonSimulation);
                    break;
                case LICENIE:
                    event = new EventLicenieStart(zakaznikSalonu, time, salonSimulation);
                    break;
                case RECEPCIA:
                    event = new EventRecepciaStart(zakaznikSalonu, time, salonSimulation);
                    break;
            }
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
                    event = new EventUcesStart(zakaznikSalonu, time, salonSimulation);
                    break;
                case LICENIE:
                    event = new EventLicenieStart(zakaznikSalonu, time, salonSimulation);
                    break;
                case RECEPCIA:
                    event = new EventRecepciaStart(zakaznikSalonu, time, salonSimulation);
                    break;
            }
            salonSimulation.addToKalendar(event);
        } else {
            rad.add(zakaznikSalonu);
        }
    }


}
