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

    public Integer getReadyTime() {
        return readyTime;
    }

    /**
     * Smaller readyTime has higher priority, if ready times are the same, smaller code has higher priority.
     *
     * @param o the object to be compared.
     * @return [0, 1, -1]
     */
    @Override
    public int compareTo(Flight o) {
        return readyTime.equals(o.readyTime) ? code.compareTo(o.code) : readyTime.compareTo(o.readyTime);
    }
}