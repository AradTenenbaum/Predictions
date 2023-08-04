package com.predict.engine.def;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private String name;
    private int ticks = 1;
    private double probability = 1;
    private List<Action> actions;

    public Rule(String name, List<Action> actions) {
        this.name = name;
        this.actions = actions;
    }

    public Rule(String name, int ticks, double probability, List<Action> actions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "name='" + name + '\'' +
                ", ticks=" + ticks +
                ", probability=" + probability +
                ", actions=" + actions +
                '}';
    }
}
