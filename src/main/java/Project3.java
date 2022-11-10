package main.java;

public class Project3 {
    public static void main(String[] args) { // discrete event simulation project.

        // Input

        // Process

        // Output

    }
}

/*
    Pre-computation phase: Find the connected airport components and add an ATC to each component.

    Discrete Event Simulation phase: Simulate the arrival and departure of flights. Compute this phase
    until all flights have landed. ATC will have to do the procedures, and it should implement some type of
    job scheduling, "round-robin" might be the best option.

    Some flights might be emergencies. These will produce an "interrupt" and start computing right away.

    Hashing: 

    Post-computation phase: Compute the average wait time for each flight.

    Output: Print the average wait time for each flight.

 */


/*
    The airport is a directed graph. The nodes are the airports, and the edges are the flights.
    The edges have a weight, which is the time it takes to fly from one airport to another.

 */