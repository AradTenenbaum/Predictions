package com.predict.engine.utils.object;

import java.util.Random;

public class Range {
    private double from;
    private double to;

    public Range(double from, double to) {
        this.from = from;
        this.to = to;
    }

    public int random() {
        Random rand = new Random();
        int randomInteger = (int)Math.floor(Math.random() * (to - from + 1) + to);
        return randomInteger;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Range{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}