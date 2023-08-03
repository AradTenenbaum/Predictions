package com.predict.engine.def;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private String name;
    private Map<String, Property> properties = new HashMap<>();
    private int population;

    public Entity(String name, int population) {
        this.name = name;
        this.population = population;
    }
}
