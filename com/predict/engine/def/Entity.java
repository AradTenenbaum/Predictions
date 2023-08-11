package com.predict.engine.def;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Entity implements Serializable {
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

    public Map<String, Property> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
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
