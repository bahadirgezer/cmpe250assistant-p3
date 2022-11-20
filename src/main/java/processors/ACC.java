package main.java.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
public class ACC {
    private Integer time;

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
    private final HashMap<String, ATC> atcs;

    /**
     * Hash table airports in this ACC <br>
     */
    private final List<String> table = Arrays.asList(new String[1000]);

    public ACC(String code) {
        this.code = code;
        this.atcs = new HashMap<>();
        this.time = 0;
    }

    public void addAtc(String airportCode, ATC atc) {
        this.atcs.put(airportCode, atc);
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(int end) {
        this.time = end;
    }

    public Integer getAtcTime(String atcCode) {
        return atcs.get(atcCode).getTime();
    }

    public void setAtcTime(String atcCode, int time) {
        atcs.get(atcCode).setTime(time);
    }

    public String getCode() {
        return code;
    }

    /**
     * @return "{acc code} {time} {average wait time} {maximum wait queue length}"
     */
    public String toString() {
        return String.format("%s %d", this.code, this.time) + this.getAirportSlots();
    }

    /**
     * ATC code generator.
     *
     * @param airportCode - Airport code
     */
    public void generateAtcCode(String airportCode) { // return "{AccCode}{hash(AirportCode)"
        hashAirportCode(airportCode);
    }

    private void hashAirportCode(String airportCode) {
        int val = (int)airportCode.charAt(0) +
                (int)airportCode.charAt(1) * 31 +
                (int)airportCode.charAt(2) * 31 * 31; // ASCII value hash function

        int i = 0;
        val %= 1000;
        while (table.get(val) != null) { // collision, linear probing
            val = (val + i) % 1000;
            i++;
        }
        table.set(val, airportCode);
    }

    /**
     * @return "{AirportCode}{hash(AirportCode)} {AirportCode}{hash(AirportCode)} ..."
     */
    private String getAirportSlots() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            if (table.get(i) != null) {
                sb.append(String.format(" %s%03d", table.get(i), i));
            }
        }
        return sb.toString();
    }
}
