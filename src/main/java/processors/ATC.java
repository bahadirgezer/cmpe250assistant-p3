package main.java.processors;

import main.java.entities.Airport;
import main.java.enums.ProcessorStatus;

public class ATC { // Air Traffic Control

    private final String code; // "{AccCode}{hash(AirportCode)}"
    private final ACC acc; // Area Control Center
    private final Airport airport; // Airport

    private ProcessorStatus state; // Processor state
    public ATC(ACC acc, Airport airport) {
        this.acc = acc;
        this.airport = airport;
        this.code = acc.generateAtcCode(airport.getCode());
        state = ProcessorStatus.IDLE;
    }


}

