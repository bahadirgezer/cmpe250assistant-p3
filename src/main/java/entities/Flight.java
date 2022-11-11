package main.java.entities;

import main.java.enums.Priority;
import main.java.enums.State;


public class Flight {
    private Airport origin;
    private Airport destination;
    private String code;
    private State state;
    private Priority priority;


    public Priority getPriority() {
        return priority;
    }

}