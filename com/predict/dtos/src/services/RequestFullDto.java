package services;

public class RequestFullDto extends RequestDto {
    private String username;
    private int id;
    private String status;

    public RequestFullDto(String simulationName, int runs, int ticks, int seconds, boolean isStopByUser, String username, int id, String status) {
        super(simulationName, runs, ticks, seconds, isStopByUser);
        this.username = username;
        this.id = id;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }
}
