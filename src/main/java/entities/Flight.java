package main.java.entities;

import java.util.ArrayList;

public class Flight implements Comparable<Flight> {


    private final Integer admissionTime;

    /**
     * @id - Flight code <br>
     */
    private final String code;
    private final String accCode;
    private final String origin;
    private final String destination;

    /**
     * index -> time <br>
     *  0 -> ACC initial processing time (r) <br>
     *  1 -> Passing flight information to ACC time (w) <br>
     *  2 -> ACC transfer control to ATC processing time (r) (ACC -> departure ATC) (ACC wait until ATC termination) <br>
     *  3 -> initial ATC processing time (r) <br>
     *  4 -> boarding wait time (w) <br>
     *  5 -> taxi information processing time (r) <br>
     *  6 -> taxi wait time (w) <br>
     *  7 -> takeoff clearance processing time (r) <br>
     *  8 -> takeoff && getting away from the ATC control area wait time (w) <br>
     *  9 -> transfer control to ACC processing time (r) (departure ATC -> ACC) (departure ATC terminate) <br>
     * 10 -> ACC take control back and flight path processing time (r) <br>
     * 11 -> flight wait time (w) <br>
     * 12 -> ACC transfer control to ATC processing time (r) (ACC -> ATC) (ACC wait until ATC termination) <br>
     * 13 -> ATC initial processing time && runway information (r) <br>
     * 14 -> flight landing wait time (w) <br>
     * 15 -> taxi information processing time (r) <br>
     * 16 -> taxi wait time (w) <br>
     * 17 -> gate information processing time (r) <br>
     * 18 -> disembark wait time (w) <br>
     * 19 -> terminal processing time (r) (landing ATC -> ACC) (landing ATC terminate) <br>
     * 20 -> ACC take control back && file flight record processing time (r) (ACC terminate) <br>
     */
    private ArrayList<Integer> operationTimes;

    public Flight(int admissionTime,
                  String code,
                  String accCode,
                  String origin,
                  String destination,
                  ArrayList<Integer> operationTimes) {

        this.admissionTime = admissionTime;
        this.code = code;
        this.accCode = accCode;
        this.origin = origin;
        this.destination = destination;
        this.operationTimes = operationTimes;
    }

    public Integer getAdmissionTime() {
        return admissionTime;
    }

    public String getCode() {
        return code;
    }

    public String getAccCode() {
        return accCode;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public int compareTo(Flight o) {
        return this.admissionTime.compareTo(o.admissionTime);
    }
}