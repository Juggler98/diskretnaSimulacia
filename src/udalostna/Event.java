package udalostna;

import simCores.EventCore;

public abstract class Event implements Comparable<Event> {

    private final Double time;
    private EventCore eventCore;

    public Event(double time, EventCore eventCore) {
        this.time = time;
        this.eventCore = eventCore;
    }

    public abstract void vykonaj();

    public Double getTime() {
        return time;
    }

}
