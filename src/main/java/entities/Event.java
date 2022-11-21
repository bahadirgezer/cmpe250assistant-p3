package main.java.entities;

import main.java.processors.ACC;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static main.java.Project3.accs;
import static main.java.processors.DES.TIME_QUANTUM;

public class Event implements Comparable<Event> {

    private final Integer time;
    private final Flight flight;

    public Event(int time, Flight flight) {
        this.time = time;
        this.flight = flight;
    }

    public Event process() {
        ACC acc = accs.get(flight.getAccCode());
        int start, runTime, end, opTime;
        String atcCode;
        switch (flight.getOperationCount()) {
            case 21, 19, 11, 9, 1 -> { // ACC -Running
                start = max(time, acc.getTime());
                opTime = flight.getTime();
                runTime = min(opTime, TIME_QUANTUM);
                end = start + runTime;
                acc.setTime(end);
                flight.setTime(opTime - runTime);
                return new Event(end, flight);
            }
            case 20, 17, 15, 13, 10, 7, 5, 3 -> { // Waiting
                end = time + flight.getTime();
                flight.setTime(0);
                return new Event(end, flight);
            }
            case 18, 16, 14, 12 -> { // ATC Departure - Running
                atcCode = flight.getDeparture();
                start = max(time, acc.getAtcTime(atcCode));
                opTime = flight.getTime();
                end = start + opTime;
                acc.setAtcTime(atcCode, end);
                flight.setTime(0);
                return new Event(end, flight);
            }
            case 8, 6, 4, 2 -> { // ATC Arrival - Running
                atcCode = flight.getArrival();
                start = max(time, acc.getAtcTime(atcCode));
                opTime = flight.getTime();
                end = start + opTime;
                acc.setAtcTime(atcCode, end);
                flight.setTime(0);
                return new Event(end, flight);
            }
            case 0 -> { // Finished
                acc.setTime(time);
                return null;
            }
            default -> throw new IllegalStateException(
                    "Unexpected value: " + flight.getOperationCount());
        }
    }

    /**
     * Event with the earliest time is smaller, if times are equal,
     * flight with the smaller code is smaller
     *
     * @param o the object to be compared.
     * @return [0, 1, -1]
     */
    @Override
    public int compareTo(Event o) {
        return time.equals(o.time) ?
                flight.getCode().compareTo(o.flight.getCode()) :
                time.compareTo(o.time);
    }
}
