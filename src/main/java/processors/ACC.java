package main.java.processors;

import main.java.entities.Flight;

import java.util.*;

public class ACC { // Area Control Center // controls the area

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
    private List<String> table = Arrays.asList(new String[100]); // hash table for ATC codes

    private ArrayDeque<Flight> flightAdmissionQueue; // queue for flights waiting to be admitted

    public ACC(String code) {
        this.code = code;
        this.ATCs = new HashMap<>();
    }

    public void admitFlight(Flight flight) {
        // add to the front remove from the back
        flightAdmissionQueue.addFirst(flight);
    }


    public String generateAtcCode(String airportCode) { // return "{AccCode}{hash(AirportCode)"
        return this.code + hashAirportCode(airportCode);
    }

    private String hashAirportCode(String airportCode) {
        int val = airportCode.charAt(0) + airportCode.charAt(1) + airportCode.charAt(2); // ASCII values
        int i = 0;
        while (table.get(val) != null) { // collision, quadratic probing
            val = (val + i*i) % 100;
            i++;
        }
        table.set(val, airportCode);
        return String.format("%02d", val);
    }

    public void addATC(String airportCode, ATC atc) {
        this.ATCs.put(airportCode, atc);
    }

    public void processFlights() {
    }

    public String toString() {
        return "";
    }
}

