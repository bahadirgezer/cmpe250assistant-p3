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

    private final ArrayDeque<Flight> flightAdmissionQue; // queue for flights waiting to be admitted

    private final ArrayDeque<Flight> readyQue; // queue for flights ready to be processed

    private final PriorityQueue<Flight> waitQue; // queue for flights waiting to be ready

    public ACC(String code) {
        this.code = code;
        this.ATCs = new HashMap<>();
        this.flightAdmissionQue = new ArrayDeque<>();
        this.readyQue = new ArrayDeque<>();
        this.waitQue = new PriorityQueue<>(Comparator.comparingInt(Flight::getWaitEndTime));
    }

    public void processFlights() {
        if (flightAdmissionQue.isEmpty())
            return;
        readyQue.add(flightAdmissionQue.pop());

        int time = readyQue.peek().getAdmissionTime();
        while (!readyQue.isEmpty() &&
                !waitQue.isEmpty() && // TODO: check if this is correct
                !flightAdmissionQue.isEmpty()) {

            Flight flight = readyQue.pop();
            time += flight.process(TIME_QUANTUM);
            admitFlights(time);

            switch (flight.getStatus()) {
                case READY:
                    readyQue.add(flight);
                    break;
                case WAITING:
                    // TODO: wait queue thing
                    waitQue.add(flight);
                    break;
                case TAKEOFF:
                    // TODO: takeoff
                    ATCs.get(flight.getOrigin()).takeoff(flight);
                    break;
                case LANDING:
                    // TODO: land
                    ATCs.get(flight.getDestination()).land(flight);
                    break;
                case FINISHED:
                    break;
            }
        }
    }

    private void admitFlights(Integer untilTime) {
        while (!flightAdmissionQue.isEmpty() &&
                flightAdmissionQue.peek().getAdmissionTime() <= untilTime)
            readyQue.add(flightAdmissionQue.pop());
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
        int val = airportCode.charAt(0) * 10000 + airportCode.charAt(1) * 100 + airportCode.charAt(2); // ASCII values
        int i = 0;
        while (table.get(val) != null) { // collision, quadratic probing
            val = (val + i*i) % 10000;
            i++;
        }
        table.set(val, airportCode);
        return String.format("%04d", val);
    }

    public void addATC(String airportCode, ATC atc) {
        this.ATCs.put(airportCode, atc);
    }

    public void addFlight(Flight flight) {
        // add to the back remove from the front
        flightAdmissionQue.add(flight);
    }

    public String toString() {
        return "";
    }
}

