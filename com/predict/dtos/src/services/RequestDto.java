package services;

public class RequestDto {
    private String simulationName;
    private int runs;
    private int ticks;
    private int seconds;
    private boolean isStopByUser;

    public RequestDto(String simulationName, int runs, int ticks, int seconds, boolean isStopByUser) {
        this.simulationName = simulationName;
        this.runs = runs;
        this.ticks = ticks;
        this.seconds = seconds;
        this.isStopByUser = isStopByUser;
    }

    public RequestDto() {
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
}
