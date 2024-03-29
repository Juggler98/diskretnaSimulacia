package simCores;

import udalostna.Event;
import udalostna.gui.ISimDelegate;
import udalostna.EventSystem;

import java.util.LinkedList;
import java.util.PriorityQueue;

public abstract class EventCore extends MonteCarlo {

    private final PriorityQueue<Event> kalendarUdalosti = new PriorityQueue<>();
    private boolean stopped = false;
    private double simTime = 0;
    private boolean paused = false;
    private int sleepTime = 0;

    private final LinkedList<ISimDelegate> delegates = new LinkedList<>();

    public EventCore() {

    }

    public synchronized void simulateEvents(double endTime) {
        simTime = 0;
        stopped = false;
        EventSystem eventSystem = new EventSystem(simTime, this);
        kalendarUdalosti.add(eventSystem);
        while (!kalendarUdalosti.isEmpty() && !stopped && (endTime == 0 || simTime <= endTime)) {
            Event event = kalendarUdalosti.poll();
            simTime = event.getTime();
            event.vykonaj();
            refreshGUI();
            while (paused) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void registerDelegate(ISimDelegate delegate) {
        delegates.add(delegate);
    }

    protected void refreshGUI() {
        for (ISimDelegate delegate : delegates) {
            delegate.refresh(this);
        }
    }

    public void stopEvents() {
        stopped = true;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void addToKalendar(Event e) {
        if (e.getTime() < simTime)
            throw new IllegalStateException("(EventTime < SimTime) - " + e + " SimTime: " + simTime);
        kalendarUdalosti.add(e);
    }

    public void clearKalendar() {
        kalendarUdalosti.clear();
    }

    public int getKalendarSize() {
        return kalendarUdalosti.size();
    }

    public double getSimTime() {
        return simTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setSimTimeToZero() {
        this.simTime = 0;
    }

}
