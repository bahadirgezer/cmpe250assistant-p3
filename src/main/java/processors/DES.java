package main.java.processors;


import main.java.entities.Event;
import main.java.entities.Flight;

import java.util.PriorityQueue;

import static main.java.Project3.flights;

public class DES {

    public final static int TIME_QUANTUM = 30;
    private static PriorityQueue<Event> eventQueue;

    public static StringBuilder log;

    public DES() {
        eventQueue = new PriorityQueue<>();
        log = new StringBuilder();
        while (!flights.isEmpty())
            addFlight(flights.poll());
    }

    private void addFlight(Flight flight) {
        Event event = new Event(flight.getEntryTime(), flight);
        eventQueue.add(event);
    }

    public void run() {
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            Event nextEvent = event.process();
            if (nextEvent != null)
                eventQueue.add(nextEvent);
        }
    }

    public String getLog() {
        return log.toString();
    }
}
