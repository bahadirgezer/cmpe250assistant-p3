package main.java.enums;

public enum State {
    ACC_NEW,
    ACC_READY,
    ACC_RUNNING,
    ACC_WAITING,
    ACC_TERMINATED,

    D_ATC_NEW,
    D_ATC_READY,
    D_ATC_RUNNING,
    D_ATC_WAITING,
    D_ATC_TERMINATED,

    A_ATC_NEW,
    A_ATC_READY,
    A_ATC_RUNNING,
    A_ATC_WAITING,
    A_ATC_TERMINATED;

    /**
     * Returns the name of the enum constant, as contained in the
     * declaration.
     * @return
     */
    @Override
    public String toString() {
        return name();
    }
}


