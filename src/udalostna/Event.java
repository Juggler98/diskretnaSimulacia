package udalostna;

import simCores.EventCore;

public abstract class Event implements Comparable<Event> {

    private final Double time;
    private EventCore eventCore;
    private String name;

    public Event(double time, EventCore eventCore, String name) {
        this.time = time;
        this.eventCore = eventCore;
        this.name = name;
    }

    public abstract void vykonaj();

    public Double getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                " time=" + time / 3600 +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Event o) {
        return time.compareTo(o.time);
    }
}
