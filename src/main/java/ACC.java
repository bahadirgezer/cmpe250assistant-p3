package main.java;

import java.util.HashSet;

public class ACC { // Area Control Center // controls the area

    private final String code;
    private final HashSet<ATC> ATCs;

    public ACC(String code, HashSet<ATC> ATCs) {
        this.code = code;
        this.ATCs = ATCs;
    }

    /*
    public ACC(String code, String ATCs) {
        this.code = code;
        this.ATCs = new TreeSet<>(Arrays.stream(ATCs.split("\\s+")).map(ATC::new).toList());
    }
    */

    public ACC(String code) {
        this.code = code;
        this.ATCs = new HashSet<>();
    }


    public String getCode() {
        return code;
    }

    public HashSet<ATC> getATCs() {
        return ATCs;
    }
}
