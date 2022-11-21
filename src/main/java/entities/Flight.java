package main.java.entities;

import main.java.processors.ACC;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Map;

public class Flight {

    private final String code;
    private final Integer entry;
    private final String accCode;
    private final String departure;
    private final String arrival;

    /**
     * <b>size -> State</b> <br>
     * 21 -> ACC - Running <br>
     * 20 -> ACC - Waiting <br>
     * 19 -> ACC - Running <br>
     * 18 -> ATC Departure - Running <br>
     * 17 -> ATC Departure - Waiting <br>
     * 16 -> ATC Departure - Running <br>
     * 15 -> ATC Departure - Waiting <br>
     * 14 -> ATC Departure - Running <br>
     * 13 -> ATC Departure - Waiting <br>
     * 12 -> ATC Departure - Running <br>
     * 11 -> ACC - Running <br>
     * 10 -> ACC - Waiting <br>
     *  9 -> ACC - Running <br>
     *  8 -> ATC Arrival - Running <br>
     *  7 -> ATC Arrival - Waiting <br>
     *  6 -> ATC Arrival - Running <br>
     *  5 -> ATC Arrival - Waiting <br>
     *  4 -> ATC Arrival - Running <br>
     *  3 -> ATC Arrival - Waiting <br>
     *  2 -> ATC Arrival - Running <br>
     *  1 -> ACC - Running <br>
     *  0 -> Finished <br>
     */
    private final ArrayDeque<Integer> operationTimes;

    public Flight(Integer admissionTime,
                  String code,
                  String accCode,
                  String origin,
                  String destination,
                  ArrayDeque<Integer> operationTimes) {
        this.code = code;
        this.accCode = accCode;
        this.departure = origin;
        this.arrival = destination;
        this.operationTimes = operationTimes;
        this.entry = admissionTime;
    }

    public String getCode() {
        return code;
    }

    public String getAccCode() {
        return accCode;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public int getEntryTime() {
        return entry;
    }

    public Integer getOperationCount() {
        return operationTimes.size();
    }

    public Integer getTime() {
        return operationTimes.pop();
    }

    public void setTime(int time) {
        if (time > 0)
            operationTimes.push(time);
    }
}