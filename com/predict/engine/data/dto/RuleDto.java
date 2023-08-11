package com.predict.engine.data.dto;

import java.io.Serializable;
import java.util.List;

public class RuleDto implements Serializable {
    private String name;
    private Double probability;
    private int ticks;
    private List<String> actions;

    public RuleDto(String name, Double probability, int ticks, List<String> actions) {
        this.name = name;
        this.probability = probability;
        this.ticks = ticks;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public Double getProbability() {
        return probability;
    }

    public int getTicks() {
        return ticks;
    }

    public List<String> getActions() {
        return actions;
    }
}
