package com.predict.engine.def;

public class Rule {
    private String name;
    private int ticks;
    private double probability;
    private Action action;

    public Rule(String name, int ticks, double probability, Action action) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.action = action;
    }
}
