package main.java;

import main.java.entities.Airport;
import main.java.entities.Flight;
import main.java.processors.ACC;
import main.java.processors.ATC;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project3 {

    public static Integer time = 0;
    static HashMap<String, Airport> airports = new HashMap<>();
    static HashMap<String, ACC> accs = new HashMap<>();
    static HashMap<String, ATC> atcs = new HashMap<>();
    static ArrayList<Flight> flights = new ArrayList<>();
    public static void main(String[] args) { // discrete event simulation project.

        /* Input */

        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new FileReader(args[0]));
            bw = new BufferedWriter(
                    new FileWriter(args[1]));

        } catch (FileNotFoundException e) {
            System.err.println("Exception caught: Input file not found.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Exception caught: Output file could not be opened.");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Exception caught: Input and output file paths must be provided.");
            System.exit(1);
        }

        String line;
        List<String> tokens;
        try {
            tokens = List.of(br.readLine().split("\s"));
            int A = Integer.parseInt(tokens.get(0));
            int F = Integer.parseInt(tokens.get(1));
            airports = new HashMap<>(A);
            flights = new ArrayList<>(F);


            for (int i = 0; i < A; i++) {
                if ((line = br.readLine()) == null) {
                    System.err.println("Exception caught: Input line could not be read.");
                    System.exit(1);
                }
                tokens = List.of(line.split("\s"));
                String airportCode = tokens.get(0);
                List<String> connections = tokens.subList(1, tokens.size());
                Airport airport = new Airport(airportCode, connections);
                airports.put(airportCode, airport);
            }

            for (int i = 0; i < F; i++) {
                if ((line = br.readLine()) == null) {
                    System.err.println("Exception caught: Input line could not be read.");
                    System.exit(1);
                }
                tokens = List.of(line.split("\s"));
                String flightCode = tokens.get(0);
                String origin = tokens.get(1);
                String destination = tokens.get(2);
                ArrayList<Integer> opTimes = tokens.stream()
                        .skip(3)
                        .mapToInt(Integer::parseInt)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                Airport originAirport = airports.get(origin);
                Airport destinationAirport = airports.get(destination);
                Flight flight = new Flight(flightCode, originAirport, destinationAirport, opTimes);
                flights.add(flight);
            }

        } catch (IOException e) {
            System.err.println("Exception caught: Input file could not be read.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.err.println("Exception caught: Inputs must be integer values.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Exception caught: Incorrect input format.");
        }

        try {
            br.close();
        } catch (IOException e) {
            System.err.println("Exception caught: Input file could not be closed.");
            System.exit(1);
        }
        /* End of Input */

        /* Process */
        StringBuilder sb = new StringBuilder();

        // Create one ACC for each connected component of the airport graph.
        // Airport graph is undirected. Each connection is directed but in both directions.
        // Each ACC is responsible for all airports in its connected component.



        /* End of Process */

        /* Output */
        try {
            bw.write(sb.toString());
        } catch (IOException e) {
            System.err.println("Exception caught: Output file could not be written.");
            System.exit(1);
        }

        try {
            bw.close();
        } catch (IOException e) {
            System.err.println("Exception caught: Output file could not be closed.");
            System.exit(1);
        }
        /* End of Output */
    }

    public HashMap<String, ACC> createAirTrafficControl(ArrayList<String> accCodes) {
        for (Airport airport : airports.values()) {
            if (!airport.hasATC()) {
                ACC acc = new ACC();
                createAreaControlCenter(airport, acc);


            }
        }

        return null;
    }

    public HashMap<String, ACC> createAreaControlCenter(Airport airport,
                                                        ACC acc) {
        for ()

        return null;
    }

    /*
        int find_set(int v) {
            if (v == parent[v])
                return v;
            return parent[v] = find_set(parent[v]);
        }

        void make_set(int v) {
            parent[v] = v;
            size[v] = 1;
        }

        void union_sets(int a, int b) {
            a = find_set(a);
            b = find_set(b);
            if (a != b) {
                if (size[a] < size[b])
                    swap(a, b);
                parent[b] = a;
                size[a] += size[b];
            }
        }

     */
}

/*
    Input file format:
    1. First line: <number of airports> <number of flights>
    2. Next <number of airports> lines: <airport code> <list of connections>
    3. Next <number of flights> lines: <flight code> <departure airport code> <arrival airport code> <list of operation times>
 */

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


/*
    Enter from new, exit from terminated.

    ===== ACC =====

    ACC scheduling -> Round Robin
    ACC states:
            -new
            -ready
            -running
            -waiting
            -terminated

    created:            new     -> ready
    dispatch:           ready   -> running
    interrupt:          running -> ready
    wait:               running -> waiting
    exit:               running -> terminated
    wait completion:    waiting -> ready

    created -> new Flight input: adds the Flight into correct ACC ready queue
        ready: waiting for dispatch, in the correct ACC ready queue

    dispatch -> start processing flight and block ACC
        running: check for clearances, flight plan, flight information, etc.

    interrupt -> scheduling interrupt and unblock ACC

    wait -> pass operation to *departing* ATC and wait for output, and unblock ACC

    wait -> pass operation to *landing* ATC and wait for output, and unblock ACC

    wait -> ask plane for flight details, wait for output, and unblock ACC

    exit -> remove flight from the cycle. Unblock ACC.
        terminated: flight operations are complete.

    wait completion -> reenter ready queue.

    ===== ATC =====

    ATC scheduling ->
    ATC states:
            -new
            -ready
            -running
            -waiting
            -terminated

    created:           new     -> ready
    dispatch:          ready   -> running
    interrupt:         running -> ready
    wait:              running -> waiting
    exit:              running -> terminated
    wait completion:   waiting -> ready

    created -> new Flight input: adds the Flight into correct ACC ready queue
        ready: waiting for dispatch, in the correct ACC ready queue

    dispatch -> start processing flight and block ATC
        running:
            for landings [get control / landing information, (landing wait), taxi/gate information, (taxi wait),
            disembark/baggage information processing, (disembark/baggage), all successful/wrap up and end communication],

            for departures [get control / boarding/ baggage, (boarding / baggage wait), takeoff information / flight
            plan, (affirmative wait), taxi information, (pushback / taxi wait), ready / waiting to be cleared for
            takeoff, (takeoff), all successful/wrap up and end communication/ pass back to AAC]

    interrupt -> scheduling interrupt and unblock ATC
    interrupt -> mayday, emergency, etc.

    wait -> specified above, and unblock ATC]

    exit -> remove flight from the cycle. Unblock ATC.
        terminated: flight operations are complete.

    wait completion -> reenter ready queue.
 */