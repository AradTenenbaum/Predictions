package com.predict.engine.def;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World implements Serializable {
    private Environment environment;
    private Map<String, Entity> entities = new HashMap<>();
    private List<Rule> rules = new ArrayList<>();
    private Termination termination;

    public World(Environment environment, Map<String, Entity> entities, List<Rule> rules, Termination termination) {
        this.environment = environment;
        this.entities = entities;
        this.rules = rules;
        this.termination = termination;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Map<String, Entity> getEntities() {
        return entities;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Termination getTermination() {
        return termination;
    }

    public World() {
    }
}
