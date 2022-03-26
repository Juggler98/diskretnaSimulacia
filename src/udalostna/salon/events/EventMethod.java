package udalostna.salon.events;

import udalostna.Event;
import udalostna.salon.SalonSimulation;
import udalostna.salon.pracoviska.Pracovisko;
import udalostna.salon.pracoviska.Zamestnanec;
import udalostna.salon.zakaznik.ZakaznikSalonu;

import java.util.Queue;

public abstract class EventMethod {

    public static boolean planStart (SalonSimulation salonSimulation, Queue<ZakaznikSalonu> rad, Pracovisko pracovisko, EventStartType eventStartType, double time) {
        if (!rad.isEmpty() && pracovisko.jeNiektoVolny()) {
            ZakaznikSalonu zakaznikSalonu = rad.poll();
            Zamestnanec zamestnanec = pracovisko.obsadZamestnanca();
            Event event = null;
            switch (eventStartType) {
                case UCES:
                    event = new EventUcesStart(zakaznikSalonu, time, salonSimulation, zamestnanec);
                    break;
                case LICENIE:
                    event = new EventLicenieStart(zakaznikSalonu, time, salonSimulation, zamestnanec);
                    break;
                case RECEPCIA:
                    event = new EventRecepciaStart(zakaznikSalonu, time, salonSimulation, zamestnanec);
                    break;
            }
            salonSimulation.addToKalendar(event);
            return true;
        }
        return false;
    }


}
