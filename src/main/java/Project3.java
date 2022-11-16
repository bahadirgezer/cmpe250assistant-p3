package main.java;

import main.java.entities.Airport;
import main.java.entities.Flight;
import main.java.processors.ACC;
import main.java.processors.ATC;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class Project3 {

    public static Logger logger = Logger.getLogger(Project3.class.getName());
    public static Integer time = 0;
    private static HashMap<String, Airport> airports;
    private static HashMap<String, ACC> accs;
    private static HashMap<String, ATC> atcs;
    private static ArrayList<Flight> flights;

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
        Deque<String> accCodes = null;
        try {
            tokens = List.of(br.readLine().split("\s"));

            /* number of airports */
            int A = Integer.parseInt(tokens.get(0));

            /* number of flights */
            int F = Integer.parseInt(tokens.get(1));

            /* number of area control centers */
            int C = Integer.parseInt(tokens.get(2));

            airports = new HashMap<>(A);
            atcs = new HashMap<>(A);
            flights = new ArrayList<>(F);
            accs = new HashMap<>(C);
            accCodes = new ArrayDeque<>(C);
            

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
                Integer admissionTime = Integer.parseInt(tokens.get(1));

                String origin = tokens.get(2);
                Airport originAirport = airports.get(origin);

                String destination = tokens.get(3);
                Airport destinationAirport = airports.get(destination);

                ArrayList<Integer> opTimes = tokens.stream()
                        .skip(4)
                        .mapToInt(Integer::parseInt)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                Flight flight = new Flight(flightCode, admissionTime, originAirport, destinationAirport, opTimes);
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

        // Create ACCs and ATCs
        createACCs(accCodes);
        accCodes = null; // help garbage collector

        // sort flights in flights arraylist by admission time in reverse order by using the comparable interface
        flights.sort(Comparator.reverseOrder());

        for (Flight flight : flights) {

        }

        // Main process loop
        for (ACC acc : accs.values()) {
            
        }

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

    /**
     * Creates the ACCs for the connected components of the airport graph.
     * Airports will be searched in the order of their airport codes.
     *
     * @param accCodes The codes of the ACCs to be created. Is unique.
     */
    private  static void createACCs(Deque<String> accCodes) {
        for (Airport airport : airports.values().stream().sorted().toList()) {
            if (!airport.hasATC()) {
                ACC acc = new ACC(accCodes.pop());
                accs.put(acc.getCode(), acc);
                createATCs(airport, acc);
            }
        }
    }

    /**
     * Creates an Area Control Center for the given airport and adds it to the given ACC.
     * Works like a DFS on the airport graph.
     *
     * @param airport The airport to create an ATC for.
     * @param acc The ACC to add the ATC to.
     */
    private static void createATCs(Airport airport, ACC acc) {
        ATC initialAtc = new ATC(acc, airport);
        airport.assignATC();
        atcs.put(initialAtc.getCode(), initialAtc);

        for (String connectedAirportCode : airport.getConnections()) {
            Airport connectedAirport = airports.get(connectedAirportCode);
            if (!connectedAirport.hasATC()) {
                createATCs(connectedAirport, acc);
            }
        }
    }
}

/*
    Input file format:
    1. First line: <number of airports> <number of flights>
    2. Next <number of airports> lines: <airport code> <ACC code> <list of connections>
    3. Next <number of flights> lines: <flight code> <admission time> <departure airport code> <arrival airport code> <list of operation times>

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