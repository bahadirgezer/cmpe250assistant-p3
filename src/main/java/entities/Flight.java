package main.java.entities;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Map;

public class Flight implements Comparable<Flight> {

    private Integer readyTime;

    /**
     * @id - Flight code <br>
     */
    private final String code;
    private final String accCode;
    private final String origin;
    private final String destination;
    private FlightStatus status;

    /**
     * index -> time <br>
     * 0 -> ACC initial processing time (r) <br> 21
     * 1 -> Passing flight information to ACC time (w) <br> 20
     * 2 -> ACC transfer control to ATC processing time (r) (ACC -> departure ATC) (ACC wait until ATC termination) <br> 19
     * 3 -> initial ATC processing time (r) <br> 18
     * 4 -> boarding wait time (w) <br> 17
     * 5 -> taxi information processing time (r) <br> 16
     * 6 -> taxi wait time (w) <br> 15
     * 7 -> takeoff clearance processing time (r) <br> 14
     * 8 -> takeoff && getting away from the ATC control area wait time (w) <br> 13
     * 9 -> transfer control to ACC processing time (r) (departure ATC -> ACC) (departure ATC terminate) <br> 12
     * 10 -> ACC take control back and flight path processing time (r) <br> 11
     * 11 -> flight wait time (w) <br> 10
     * 12 -> ACC transfer control to ATC processing time (r) (ACC -> ATC) (ACC wait until ATC termination) <br> 9
     * 13 -> ATC initial processing time && runway information (r) <br> 8
     * 14 -> flight landing wait time (w) <br> 7
     * 15 -> taxi information processing time (r) <br> 6
     * 16 -> taxi wait time (w) <br> 5
     * 17 -> gate information processing time (r) <br> 4
     * 18 -> disembark wait time (w) <br> 3
     * 19 -> terminal processing time (r) (landing ATC -> ACC) (landing ATC terminate) <br> 2
     * 20 -> ACC take control back && file flight record processing time (r) (ACC terminate) <br> 1
     FINISHED <br> 0
     */
    private final ArrayDeque<Integer> operationTimes;

    // <key, value> -> <operationTimes.size(), FlightStatus>
    private final Map<Integer, FlightStatus> map = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(0, FlightStatus.FINISHED),
            new AbstractMap.SimpleEntry<>(1, FlightStatus.READY),
            new AbstractMap.SimpleEntry<>(2, FlightStatus.LANDING_END),
            new AbstractMap.SimpleEntry<>(3, FlightStatus.WAITING),
            new AbstractMap.SimpleEntry<>(4, FlightStatus.READY),
            new AbstractMap.SimpleEntry<>(5, FlightStatus.WAITING),
            new AbstractMap.SimpleEntry<>(6, FlightStatus.READY),
            new AbstractMap.SimpleEntry<>(7, FlightStatus.WAITING),
            new AbstractMap.SimpleEntry<>(8, FlightStatus.LANDING_BEGIN),
            new AbstractMap.SimpleEntry<>(9, FlightStatus.READY),
            new AbstractMap.SimpleEntry<>(10, FlightStatus.WAITING),
            new AbstractMap.SimpleEntry<>(11, FlightStatus.READY),
            new AbstractMap.SimpleEntry<>(12, FlightStatus.TAKEOFF_END),
            new AbstractMap.SimpleEntry<>(13, FlightStatus.WAITING),
            new AbstractMap.SimpleEntry<>(14, FlightStatus.READY),
            new AbstractMap.SimpleEntry<>(15, FlightStatus.WAITING),
            new AbstractMap.SimpleEntry<>(16, FlightStatus.READY),
            new AbstractMap.SimpleEntry<>(17, FlightStatus.WAITING),
            new AbstractMap.SimpleEntry<>(18, FlightStatus.TAKEOFF_BEGIN),
            new AbstractMap.SimpleEntry<>(19, FlightStatus.READY),
            new AbstractMap.SimpleEntry<>(20, FlightStatus.WAITING),
            new AbstractMap.SimpleEntry<>(21, FlightStatus.READY)
    );

    public Flight(int admissionTime,
                  String code,
                  String accCode,
                  String origin,
                  String destination,
                  ArrayDeque<Integer> operationTimes) {

        this.readyTime = admissionTime;
        this.code = code;
        this.accCode = accCode;
        this.origin = origin;
        this.destination = destination;
        this.operationTimes = operationTimes;
        this.status = FlightStatus.READY;
    }

    public int process(int timeQuantum) {
        int remainingTime = operationTimes.pop();
        if (remainingTime > timeQuantum) {
            remainingTime -= timeQuantum;
            operationTimes.push(remainingTime);
            return timeQuantum;
        } else {
            readyTime += operationTimes.peek();
            status = map.get(operationTimes.size());
            return remainingTime;
        }

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

    public FlightStatus getStatus() {
        return status;
    }

    public Integer getOperationTime() {
        return operationTimes.peek();
    }

    public Integer getReadyTime() {
        return readyTime;
    }

    @Override
    public int compareTo(Flight o) {
        // smaller readyTime has higher priority, if ready times are the same, smaller code has higher priority
        return readyTime.equals(o.readyTime) ? code.compareTo(o.code) : readyTime.compareTo(o.readyTime);
    }


}