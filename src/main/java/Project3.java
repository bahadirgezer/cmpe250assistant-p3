package main.java;

import main.java.entities.Flight;
import main.java.processors.ACC;
import main.java.processors.ATC;
import main.java.processors.DES;

import java.io.*;
import java.util.*;

public class Project3 {

    public static Boolean DEBUG = false;
    public static HashMap<String, ACC> accs;
    public static ArrayDeque<Flight> flights;

    public static void main(String[] args) { // discrete event simulation project.
        Long tic = System.currentTimeMillis();

        /* Input */
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new FileReader(args[0]));

        } catch (FileNotFoundException e) {
            System.err.println("Exception caught: Input file not found.");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Exception caught: Input file path must be provided.");
            System.exit(1);
        }

        String line;
        List<String> tokens;
        try {
            tokens = List.of(br.readLine().split("\s"));
            int numACCs = Integer.parseInt(tokens.get(0));
            int numFlights = Integer.parseInt(tokens.get(1));

            flights = new ArrayDeque<>(numFlights);
            accs = new HashMap<>(numACCs);

            // Read ACCs
            for (int i = 0; i < numACCs; i++) {
                if ((line = br.readLine()) == null) {
                    System.err.println("Exception caught: Input line could not be read.");
                    System.exit(1);
                }
                tokens = List.of(line.split("\s"));
                ACC acc = new ACC(tokens.get(0));
                List<String> airportCodes = tokens.subList(1, tokens.size());

                for (String airportCode : airportCodes) {
                    ATC atc = new ATC(acc, airportCode);
                    acc.addAtc(airportCode, atc);
                }
                accs.put(acc.getCode(), acc);
            }

            // Read Flights
            for (int i = 0; i < numFlights; i++) {
                if ((line = br.readLine()) == null) {
                    System.err.println("Exception caught: Input line could not be read.");
                    System.exit(1);
                }
                tokens = List.of(line.split("\s"));

                int admissionTime = Integer.parseInt(tokens.get(0));
                String flightCode = tokens.get(1);
                String accCode = tokens.get(2);
                String departureAirportCode = tokens.get(3);
                String arrivalAirportCode = tokens.get(4);

                ArrayDeque<Integer> operationTimes = new ArrayDeque<>(
                        tokens.subList(5,
                                tokens.size()).stream()
                                .map(Integer::parseInt).toList());

                Flight flight = new Flight(
                        admissionTime,
                        flightCode,
                        accCode,
                        departureAirportCode,
                        arrivalAirportCode,
                        operationTimes);

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
        DES simulation = new DES();
        simulation.run();
        /* End of Process */

        /* Output */
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                    new FileWriter(args[1]));
        } catch (IOException e) {
            System.err.println("Exception caught: Output file could not be opened.");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Exception caught: Output file path must be provided.");
            System.exit(1);
        }

        StringBuilder sb = new StringBuilder();
        for (ACC acc : accs.values())
            sb.append(acc.toString())
                    .append(System.lineSeparator());
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

        /* Log */
        if (DEBUG) {
            BufferedWriter bwlog = null;
            try {
                bwlog = new BufferedWriter(
                        new FileWriter(args[2]));
            } catch (IOException e) {
                System.err.println("Exception caught: Log file could not be opened.");
                System.exit(1);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Exception caught: Log file path must be provided.");
                System.exit(1);
            }

            try {
                bwlog.write(simulation.getLog());
            } catch (IOException e) {
                System.err.println("Exception caught: Log file could not be written.");
                System.exit(1);
            }

            try {
                bwlog.close();
            } catch (IOException e) {
                System.err.println("Exception caught: Log file could not be closed.");
                System.exit(1);
            }
        }
        /* End of Log */

        Long toc = System.currentTimeMillis();
        if (DEBUG)
            System.out.println("Elapsed time: " + (toc - tic) + " ms");
    }
}
