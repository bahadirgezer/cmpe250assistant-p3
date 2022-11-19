package main.java.processors;

import main.java.entities.Flight;

import java.util.*;

public class ACC { // Area Control Center // controls the area

    private final static int TIME_QUANTUM = 30;

    /**
     * @id - ACC code <br>
     * 4 capital letters [A-Z]
     */
    private final String code;

    /**
     * List of ATCs assigned to this ACC <br>
     * @key - Airport code
     * @value - ATC for that airport
     */
    private final HashMap<String, ATC> ATCs;
    private final List<String> table = Arrays.asList(new String[1000]); // hash table for ATC codes

    private final ArrayDeque<Flight> readyQue; // queue for flights ready to be processed

    private final PriorityQueue<Flight> waitQue; // queue for flights waiting to be ready

    private final ArrayDeque<ATC> busyATCs; // queue for ATCs busy with a flight

    private Integer time;

    private Integer maximumQueueSize;

    private Integer totalWaitTime;

    private Integer totalFlights;

    public ACC(String code) {
        this.code = code;
        this.ATCs = new HashMap<>();
        this.readyQue = new ArrayDeque<>();
        this.waitQue = new PriorityQueue<>();
        this.busyATCs = new ArrayDeque<>();
        this.time = 0;
        this.maximumQueueSize = 0;
        this.totalWaitTime = 0;
        this.totalFlights = 0;
    }

    public void processFlights() {
        if (waitQue.isEmpty())
            return;
        readyQue.add(waitQue.poll());

        time = readyQue.peek().getReadyTime();
        while (!readyQue.isEmpty() || !waitQue.isEmpty()) {
            maximumQueueSize = Math.max(maximumQueueSize, readyQue.size());

            Flight flight;
            int timeProcessed;
            if (!readyQue.isEmpty()) {
                flight = readyQue.pop();
                timeProcessed = flight.process(TIME_QUANTUM);
                time += timeProcessed;
                admitFlights(time);
            } else {
                flight = waitQue.poll();
                time = flight.getReadyTime();
                timeProcessed = flight.process(TIME_QUANTUM);
                time += timeProcessed;
                admitFlights(time);
            }
            maximumQueueSize = Math.max(maximumQueueSize, readyQue.size());

            for (ATC atc : busyATCs) {
                atc.step(time);
                if (atc.isFree()) {
                    busyATCs.remove(atc);
                }
            }

            switch (flight.getStatus()) {
                case READY:
                    readyQue.add(flight);
                    break;
                case WAITING:
                    waitQue.add(flight);
                    break;
                case TAKEOFF_BEGIN:
                case LANDING_BEGIN:
                    ATC atc = ATCs.get(flight.getOrigin());
                    atc.addFlight(flight);
                    busyATCs.add(atc);
                    break;
                case FINISHED:
                    totalFlights++;
                    break;
            }
        }
    }

    private void admitFlights(Integer untilTime) {
        while (!waitQue.isEmpty() &&
                waitQue.peek().getReadyTime() <= untilTime) {
            readyQue.add(Objects.requireNonNull(waitQue.poll()));
        }
    }

    public void addFlight(Flight flight) {
        // add to the back remove from the front
        waitQue.add(flight);
    }

    /**
     * ATC code generator.
     *
     * @param airportCode - Airport code
     * @return ATC code : {ACC Code} + {Hashed Airport Code}
     */
    public String generateAtcCode(String airportCode) { // return "{AccCode}{hash(AirportCode)"
        return this.code + hashAirportCode(airportCode);
    }

    private String hashAirportCode(String airportCode) {
        int val = (int)airportCode.charAt(0) + (int)airportCode.charAt(1) * 31 + (int)airportCode.charAt(2) * 31 * 31; // ASCII values
        int i = 0;
        val %= 1000;
        while (table.get(val) != null) { // collision, quadratic probing
            val = (val + i) % 1000;
            i++;
        }
        table.set(val, airportCode);
        return String.format("%03d", val);
    }

    public void addATC(String airportCode, ATC atc) {
        this.ATCs.put(airportCode, atc);
    }

    public String toString() {
        // <acc code> <time> <average wait time> <maximum wait queue length>
        return String.format("%s %d", this.code, this.time) + this.getAirportSlots();
    }

    private String getAirportSlots() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            if (table.get(i) != null) {
                sb.append(String.format(" %s%03d", table.get(i), i));
            }
        }
        return sb.toString();
    }

    public String getCode() {
        return code;
    }
}

