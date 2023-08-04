package com.predict.engine.def;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private String name;
    private Map<String, Property> properties;
    private int population;

    public Entity(String name, int population) {
        this.name = name;
        this.population = population;
        this.properties = new HashMap<>();
    }

    public void addProperty(Property p) {
        properties.put(p.getName(), p);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", properties=" + properties +
                ", population=" + population +
                '}';
    }
}
