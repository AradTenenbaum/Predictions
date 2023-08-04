package com.predict.engine.def;

public class Termination {
    private int ticks;
    private int seconds;

    public Termination(int ticks, int seconds) {
        this.ticks = ticks;
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return "Termination{" +
                "ticks=" + ticks +
                ", seconds=" + seconds +
                '}';
    }
}
