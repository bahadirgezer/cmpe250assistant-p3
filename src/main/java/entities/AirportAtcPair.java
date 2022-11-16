package main.java.entities;

import main.java.processors.ATC;

import java.util.Objects;

public record AirportAtcPair(Airport airport, ATC atc) {
    public AirportAtcPair {
        Objects.requireNonNull(airport);
        Objects.requireNonNull(atc);
    }
}
