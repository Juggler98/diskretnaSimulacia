package udalostna.salon.events;

import simCores.EventCore;
import udalostna.Event;
import udalostna.salon.SalonSimulation;

public class EventSystem extends Event {

    private final SalonSimulation salonSimulation;

    public EventSystem(double time, EventCore eventCore) {
        super(time, eventCore, "EventSystem");
        this.salonSimulation = (SalonSimulation) eventCore;
    }

    @Override
    public void vykonaj() {
        //System.out.println(this);
        if (salonSimulation.getKalendarSize() > 0) {
            int sleepTime = salonSimulation.getSleepTime();
            if (sleepTime > 0) {
                if (sleepTime > 1000) {
                    for (int i = 0; i < salonSimulation.getSleepTime() / 1000; i++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                EventSystem eventSystem = new EventSystem(this.getTime() + 1, salonSimulation);
                salonSimulation.addToKalendar(eventSystem);
            }
        }
    }
}
