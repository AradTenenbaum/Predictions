package services;

public class Request {

    public enum STATUS {
        WAITING("waiting"),
        DECLINED("declined"),
        APPROVED("approved"),
        DONE("done");

        private String value;

        STATUS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    private int id;
    private static int currID = 0;
    private String username;
    private String simulationName;
    private int runs;
    private int ticks;
    private int seconds;
    private boolean isStopByUser;
    private STATUS status;


    public Request(String username, String simulationName, int runs, int ticks, int seconds, boolean isStopByUser) {
        this.id = ++currID;
        this.username = username;
        this.simulationName = simulationName;
        this.runs = runs;
        this.ticks = ticks;
        this.seconds = seconds;
        this.isStopByUser = isStopByUser;
        this.status = STATUS.WAITING;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public int getRuns() {
        return runs;
    }

    public int getTicks() {
        return ticks;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isStopByUser() {
        return isStopByUser;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public STATUS getStatus() {
        return status;
    }
}
