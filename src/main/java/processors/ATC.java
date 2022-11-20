package main.java.processors;

/**
 * @class ATC - Air Traffic Controller
 *
 */
public class ATC {

    private Integer time;

    public ATC(ACC acc, String airportCode) {
        acc.generateAtcCode(airportCode);
        this.time = 0;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}

