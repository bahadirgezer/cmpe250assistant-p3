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

    /**
     * @id - ATC code <br>
     * <br>
     * 7 characters: <br>
     * 4 capital letters [AAAA-ZZZZ] + 3 digits [000-999] <br>
     * <br>
     * 4 capital letters come from the ACC <br>
     * 3 digits come from hash function which the takes the airport code as input
     */
    private final String code;

    /**
     * @key - Flight code <br>
     * @value - Flight object
     */
    private final ACC acc;

    private final ArrayDeque<Flight> readyQue; // queue for flights ready to be processed

    private final PriorityQueue<Flight> waitQue; // queue for flights waiting to be ready

    public ATC(ACC acc, String airportCode) {
        this.acc = acc;
        this.code = acc.generateAtcCode(airportCode);
        this.readyQue = new ArrayDeque<>();
        this.waitQue = new PriorityQueue<>();
    }


    // run the ATC for untilTime seconds
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

