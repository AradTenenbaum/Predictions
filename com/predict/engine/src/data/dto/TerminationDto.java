package data.dto;

import java.io.Serializable;

public class TerminationDto implements Serializable {
    int seconds;
    int ticks;
    boolean isStoppedByUser;

    public TerminationDto(int seconds, int ticks, boolean isStoppedByUser) {
        this.seconds = seconds;
        this.ticks = ticks;
        this.isStoppedByUser = isStoppedByUser;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getTicks() {
        return ticks;
    }

    public boolean isStoppedByUser() {
        return isStoppedByUser;
    }
}
