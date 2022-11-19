package main.java.processors;

import main.java.entities.Flight;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * @class ATC - Air Traffic Controller
 *
 */
public class ATC {

    private final ACC acc;

    /**
     * Queue for flights waiting to be processed <br>
     */
    private final ArrayDeque<Flight> readyQue;

    /**
     * Queue for flights waiting to be ready <br>
     */
    private final PriorityQueue<Flight> waitQue;

    public ATC(ACC acc, String airportCode) {
        this.acc = acc;
        acc.generateAtcCode(airportCode);
        this.readyQue = new ArrayDeque<>();
        this.waitQue = new PriorityQueue<>();
    }

    public void step(int untilTime) {
        Integer time;
        while (!readyQue.isEmpty() || !waitQue.isEmpty()) {
            Flight flight;
            int timeProcessed;
            if (!readyQue.isEmpty())
                flight = readyQue.pop();
            else
                flight = waitQue.poll();

            time = flight.getReadyTime();
            timeProcessed = flight.process(untilTime);
            time += timeProcessed;
            untilTime -= timeProcessed;
            admitFlights(time);

            switch (flight.getStatus()) {
                case READY:
                    readyQue.add(flight);
                    break;
                case WAITING:
                    waitQue.add(flight);
                    break;
                case TAKEOFF_END:
                case LANDING_END:
                    acc.addFlight(flight);
                    break;
                case FINISHED:
                    break;
            }

            if (untilTime <= 0)
                break;
        }
    }

    public void addFlight(Flight flight) {
        readyQue.add(flight);
    }

    public void admitFlights(int time) {
        while (!waitQue.isEmpty() && waitQue.peek().getReadyTime() <= time) {
            readyQue.add(Objects.requireNonNull(waitQue.poll()));
        }
    }

    public boolean isFree() {
        return readyQue.isEmpty() && waitQue.isEmpty();
    }
}

