package main.java;

public class ATC { // Air Traffic Control

    String code; // "{AccCode}{hash(AirportCode)}"
    ACC acc;


    /*
        //
        hash(AirportCode, table[100]) { // hash function for ACC ATCs
            int val = AirportCode[0] + AirportCode[1] + AirportCode[2]; // ASCII values
            i = 0;
            while (table[val] != null) { // collision, quadratic probing
                val = (val + i^2) % 100;
                i++;
            }
            table[val] = AirportCode;
            return val;
        }
     */


}