package main.java.entities;

import java.util.List;
import java.util.TreeSet;

public class Airport {
    private final String code;
    private final TreeSet<String> connections;

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

    public String getCode() {
        return code;
    }
}
