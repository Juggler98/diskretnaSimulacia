package simCores;

import udalostna.Event;

import java.util.PriorityQueue;

public abstract class EventCore extends MonteCarlo {

    protected final PriorityQueue<Event> kalendarUdalosti = new PriorityQueue<>();
    private boolean stopped = false;
    private double simTime = 0;

    public EventCore() {

    }

    public void simulateEvents(double endTime) {
        simTime = 0;
        stopped = false;
        while (!kalendarUdalosti.isEmpty() && !stopped && simTime <= endTime) {
            Event event = kalendarUdalosti.poll();
            simTime = event.getTime();
            event.vykonaj();
            //System.out.println();
        }
    }

    public void stop() {
        stopped = true;
    }

    public void addToKalendar(Event e) {
        kalendarUdalosti.add(e);
    }

    public double getSimTime() {
        return simTime;
    }
}
