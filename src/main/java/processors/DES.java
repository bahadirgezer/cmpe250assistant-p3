package main.java.processors;


import main.java.entities.Event;
import main.java.entities.Flight;

import java.util.PriorityQueue;

public class DES {

    private final static int TIME_QUANTUM = 30;
    private static PriorityQueue<Event> eventQueue;

    public DES() {
        eventQueue = new PriorityQueue<>();
    }

    public void addFlight(Flight flight) {
        eventQueue.add(new Event(flight.getEntryTime(), flight));
    }

    public void run() {
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            Event nextEvent = event.process();
            if (nextEvent != null)
                eventQueue.add(nextEvent);
        }
    }

}
