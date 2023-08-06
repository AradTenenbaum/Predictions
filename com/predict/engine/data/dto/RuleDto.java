package com.predict.engine.data.dto;

import java.util.List;

public class RuleDto {
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
}
