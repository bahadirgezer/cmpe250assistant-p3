package main.java.scheduling;

import main.java.entities.Flight;
import main.java.enums.Priority;

import java.util.ArrayDeque;

/**
 * @author: Bahadir Gezer
 * @date: 2022-11-11
 * @description: Flight scheduler for ATC. Implementation of Multilevel Feedback Queue Scheduling (MLFQ) process
 * scheduling algorithm.
 */
public class AtcFlightScheduler {
    ArrayDeque<Flight> emergencyQueue; // Emergency queue. First come, first serve
    ArrayDeque<Flight> firstQueue; // Round-Robin queue with time quantum of 3
    ArrayDeque<Flight> secondQueue; // Round-Robin queue with time quantum of 6
    ArrayDeque<Flight> lastQueue; // First come, first serve queue

    public AtcFlightScheduler() {
        emergencyQueue = new ArrayDeque<>();
        firstQueue = new ArrayDeque<>();
        lastQueue = new ArrayDeque<>();
    }

    public void enqueue(Flight flight) {
    }

    public Flight dispatch() {
        if (!emergencyQueue.isEmpty()) {
            return emergencyQueue.remove();
        } else if (!firstQueue.isEmpty()) {
            // TODO
        } else if (!secondQueue.isEmpty()) {

        } else if (!lastQueue.isEmpty()) {

        } else {
            return null;
        }
        return null;
    }
}
