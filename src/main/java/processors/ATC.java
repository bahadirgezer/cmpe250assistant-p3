package main.java.processors;

import main.java.entities.Airport;
import main.java.enums.ProcessorStatus;
import main.java.scheduling.AtcFlightScheduler;

public class ATC { // Air Traffic Control

    private final String code; // "{AccCode}{hash(AirportCode)}"
    private final ACC acc; // Area Control Center
    private final Airport airport; // Airport
    private ProcessorStatus status; // Processor state

    private AtcFlightScheduler readyQueue; // Ready queue

    public ATC(ACC acc, Airport airport) {
        this.acc = acc;
        this.airport = airport;
        this.code = acc.generateAtcCode(airport.getCode());
        status = ProcessorStatus.IDLE;
    }

    public String getCode() {
        return code;
    }

}

