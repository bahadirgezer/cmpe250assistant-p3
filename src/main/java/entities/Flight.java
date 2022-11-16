package main.java.entities;

import main.java.enums.Priority;
import main.java.enums.State;

import java.util.ArrayList;


public class Flight implements Comparable<Flight> {
    private State state;

    private String flightCode;
    private AirportAtcPair origin;
    private AirportAtcPair destination;

    private Integer admissionTime;

    private Integer opCounter;

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
    private ArrayList<Integer> opTimes;

    public Flight() {
        this.state = State.ACC_NEW;
        this.opCounter = 0;
    }

    public Flight(String flightCode, Integer admissionTime, Airport origin, Airport destination, ArrayList<Integer> opTimes) {
        this.state = State.ACC_NEW;
        this.opCounter = 0;
        this.opTimes = opTimes;
        this.flightCode = flightCode;
        this.origin = origin;
        this.destination = destination;
        this.admissionTime = admissionTime;

    }

    public Integer AccNew() {
        if (this.state != state.ACC_NEW) {
            System.err.println("Invalid state transition: from " + this.state + " to ACC_NEW");
            return -1;
        }

        this.state = State.ACC_READY;
        return opTimes.get(0);
    }

    public Integer AccTerminate() {
        if (this.state != state.ACC_TERMINATED) {
            System.err.println("Invalid state transition: from " + this.state + " to ACC_TERMINATED");
            return -1;
        }

        this.state = State.ACC_TERMINATED;
        return opTimes.get(20);
    }

    public Integer AccReady() {
        if (this.state != state.ACC_READY) {
            System.err.println("Invalid state transition: from " + this.state + " to ACC_READY");
            return -1;
        }

        this.state = State.ACC_RUNNING;
        return opTimes.get(1);
    }


    @Override
    public int compareTo(Flight o) {
        return this.admissionTime.compareTo(o.admissionTime);
    }
}