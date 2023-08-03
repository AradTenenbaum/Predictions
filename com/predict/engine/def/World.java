package com.predict.engine.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    private Environment environment;
    private Map<String, Entity> entities = new HashMap<>();
    private List<Rule> rules = new ArrayList<>();
    private Termination termination;
}
