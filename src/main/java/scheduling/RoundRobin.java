package main.java.scheduling;

public class RoundRobin {
    private int quantum;
    private int time;

    public RoundRobin(int quantum) {
        this.quantum = quantum;
        this.time = 0;
    }

    public int getQuantum() {
        return quantum;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
