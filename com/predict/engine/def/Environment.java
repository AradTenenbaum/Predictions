package com.predict.engine.def;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Environment implements Serializable {
    private Map<String, Property> properties;

    public Environment() {
        this.properties = new HashMap<>();
    }

    public void addProperty(Property p) {
        properties.put(p.getName(), p);
    }

    public Map<String, Property> getProperties() {
        return properties;
    }
}
