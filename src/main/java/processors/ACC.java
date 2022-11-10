package main.java.processors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ACC { // Area Control Center // controls the area

    private final String code;
    private final HashSet<ATC> ATCs;
    private List<String> table = Arrays.asList(new String[100]); // hash table for ATC codes

    public ACC(String code, HashSet<ATC> ATCs) {
        this.code = code;
        this.ATCs = ATCs;
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
}
