package main.java.processors;

/**
 * @class ATC - Air Traffic Controller
 *
 */
public class ATC {

    /**
     * @id - ATC code <br>
     * <br>
     * 7 characters: <br>
     * 4 capital letters [AAAA-ZZZZ] + 3 digits [000-999] <br>
     * <br>
     * 4 capital letters come from the ACC <br>
     * 3 digits come from hash function which the takes the airport code as input
     */
    private final String code;

    /**
     * @key - Flight code <br>
     * @value - Flight object
     */
    private final ACC acc;


    public ATC(ACC acc, String airportCode) {
        this.acc = acc;
        this.code = acc.generateAtcCode(airportCode);
    }

    public String getCode() {
        return code;
    }

}

