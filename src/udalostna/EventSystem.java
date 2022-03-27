package udalostna;

import simCores.EventCore;

public class EventSystem extends Event {

    private final EventCore eventCore;

    public EventSystem(double time, EventCore eventCore) {
        super(time, eventCore, "EventSystem");
        this.eventCore = eventCore;
    }

    @Override
    public void vykonaj() {
        if (eventCore.getKalendarSize() > 0) {
            int sleepTime = eventCore.getSleepTime();
            if (sleepTime > 0) {
                if (sleepTime > 1000) {
                    for (int i = 0; i < eventCore.getSleepTime() / 1000; i++) {
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
                EventSystem eventSystem = new EventSystem(this.getTime() + 1, eventCore);
                eventCore.addToKalendar(eventSystem);
            }
        }
    }
}
