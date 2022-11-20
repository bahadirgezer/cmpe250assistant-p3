package main.java.entities;

import main.java.processors.ACC;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Map;

public class Flight {

    /**
     * @id - Flight code <br>
     */
    private final String code;

    private final Integer entryTime;
    private final String accCode;

    private final String origin;
    private final String destination;

    /**
     * index -> time <br>
     * 0 -> ACC initial processing time (r) 21 <br>
     * 1 -> Passing flight information to ACC time (w) 20 <br>
     * 2 -> ACC transfer control to ATC processing time (r)
     * (ACC -> departure ATC) (ACC wait until ATC termination) 19 <br>
     * 3 -> initial ATC processing time (r) 18 <br>
     * 4 -> boarding wait time (w) 17 <br>
     * 5 -> taxi information processing time (r) 16 <br>
     * 6 -> taxi wait time (w) 15 <br>
     * 7 -> takeoff clearance processing time (r) 14 <br>
     * 8 -> takeoff && getting away from the ATC control area wait time (w) 13 <br>
     * 9 -> transfer control to ACC processing time (r)
     * (departure ATC -> ACC) (departure ATC terminate) 12 <br>
     * 10 -> ACC take control back and flight path processing time (r) 11 <br>
     * 11 -> flight wait time (w) 10 <br>
     * 12 -> ACC transfer control to ATC processing time (r)
     * (ACC -> ATC) (ACC wait until ATC termination) 9 <br>
     * 13 -> ATC initial processing time && runway information (r) 8 <br>
     * 14 -> flight landing wait time (w) 7 <br>
     * 15 -> taxi information processing time (r) 6 <br>
     * 16 -> taxi wait time (w) 5 <br>
     * 17 -> gate information processing time (r) 4 <br>
     * 18 -> disembark wait time (w) 3 <br>
     * 19 -> terminal processing time (r) (landing ATC -> ACC) (landing ATC terminate) 2 <br>
     * 20 -> ACC take control back && file flight record processing time (r) (ACC terminate) 1 <br>
     FINISHED 0 <br>
     */
    private final ArrayDeque<Integer> operationTimes;

    public Flight(int admissionTime,
                  String code,
                  String accCode,
                  String origin,
                  String destination,
                  ArrayDeque<Integer> operationTimes) {

        this.code = code;
        this.accCode = accCode;
        this.origin = origin;
        this.destination = destination;
        this.operationTimes = operationTimes;
        this.entryTime = admissionTime;
    }

    public String getCode() {
        return code;
    }

    public String getDeparture() {
        return origin;
    }

    public String getArrival() {
        return destination;
    }

    public int getEntryTime() {
        return entryTime;
    }

    public String getAccCode() {
        return accCode;
    }

    public Integer getTime() {
        return operationTimes.pop();
    }

    public void setTime(int time) {
        if (time > 0)
            operationTimes.push(time);
    }

    public Integer getOperationCount() {
        return operationTimes.size();
    }
}