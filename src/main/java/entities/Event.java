package main.java.entities;

public class Event implements Comparable<Event> {
    private final Integer time;
    private final Flight flight;

    public Event(int time, Flight flight) {
        this.time = time;
        this.flight = flight;
    }

    public Event process() {
        return null;
    }

    /**
     * Evet with the earliest time is smaller, if times are equal,
     * flight with the smaller code is smaller
     *
     * @param o the object to be compared.
     * @return [0, 1, -1]
     */
    @Override
    public int compareTo(Event o) {
        return time == o.time ? flight.compareTo(o.flight) : time.compareTo(o.time);
    }
}
