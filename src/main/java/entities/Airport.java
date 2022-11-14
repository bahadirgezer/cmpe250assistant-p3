package main.java.entities;

import java.util.List;
import java.util.TreeSet;

public class Airport {
    private final String code;
    private final TreeSet<String> connections;

    private Boolean hasATC = false;

    public Airport(String code, TreeSet<String> connections) {
        this.connections = connections;
        this.code = code;
    }

    public Airport(String code) {
        this.code = code;
        this.connections = new TreeSet<>();
    }

    public Airport(String code, String connections) {
        this.code = code;
        this.connections = new TreeSet<>(List.of(connections.split("\\s+")));
    }

    public Airport(String code, List<String> connections) {
        this.code = code;
        this.connections = new TreeSet<>(connections);
    }

    public String getCode() {
        return code;
    }

    public Boolean hasATC() {
        return hasATC;
    }

    public void assignATC() {
        this.hasATC = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return code.equals(airport.code);
    }
}
