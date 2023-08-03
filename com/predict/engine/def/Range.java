package com.predict.engine.def;

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

    @Override
    public String toString() {
        return "Range{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
