package main.java.entities;

import main.java.enums.Priority;
import main.java.enums.State;

import java.util.ArrayDeque;
import java.util.ArrayList;


public class Flight {
    private State state;
    private Priority priority;
    private String flightCode;
    private Airport origin;
    private Airport destination;

    private int opCounter;

    /**
     * index -> time
     *  0 -> ACC initial processing time (r)
     *  1 -> Passing flight information to ACC time (w)
     *  2 -> ACC transfer control to ATC processing time (r) (ACC -> departure ATC) (ACC wait until ATC termination)
     *  3 -> initial ATC processing time (r)
     *  4 -> boarding wait time (w)
     *  5 -> taxi information processing time (r)
     *  6 -> taxi wait time (w)
     *  7 -> takeoff clearance processing time (r)
     *  8 -> takeoff && getting away from the ATC control area wait time (w)
     *  9 -> transfer control to ACC processing time (r) (departure ATC -> ACC) (departure ATC terminate)
     * 10 -> ACC take control back and flight path processing time (r)
     * 11 -> flight wait time (w)
     * 12 -> ACC transfer control to ATC processing time (r) (ACC -> ATC) (ACC wait until ATC termination)
     * 13 -> ATC initial processing time && runway information (r)
     * 14 -> flight landing wait time (w)
     * 15 -> taxi information processing time (r)
     * 16 -> taxi wait time (w)
     * 17 -> gate information processing time (r)
     * 18 -> disembark wait time (w)
     * 19 -> terminal processing time (r) (landing ATC -> ACC) (landing ATC terminate)
     * 20 -> ACC take control back && file flight record processing time (r) (ACC terminate)
     */
    private ArrayList<Integer> opTimes;

    public Flight() {
        this.state = State.ACC_NEW;
        this.priority = Priority.LOW;
        this.opCounter = 0;
    }

    public Flight(String flightCode, Airport origin, Airport destination, ArrayList<Integer> opTimes) {
        this.state = State.ACC_NEW;
        this.priority = Priority.LOW;
        this.opCounter = 0;
        this.opTimes = opTimes;
        this.flightCode = flightCode;
        this.origin = origin;
        this.destination = destination;

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

}