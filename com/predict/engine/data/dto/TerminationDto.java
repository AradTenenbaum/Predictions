package com.predict.engine.data.dto;

public class TerminationDto {
    int seconds;
    int ticks;

    public TerminationDto(int seconds, int ticks) {
        this.seconds = seconds;
        this.ticks = ticks;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getTicks() {
        return ticks;
    }
}
