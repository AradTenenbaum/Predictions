package def;

import utils.object.Grid;

import java.io.Serializable;
import java.util.*;

public class World implements Serializable {
    private Environment environment;
    private Map<String, Entity> entities = new HashMap<>();
    private List<Rule> rules = new ArrayList<>();
    private Termination termination;
    private int threadPoolCount;
    private String name;
    private Optional<Integer> sleep;
    private Grid grid;

    public World(Environment environment, Map<String, Entity> entities, List<Rule> rules, Termination termination, int threadPoolCount, Grid grid, String name, Integer sleep) {
        this.environment = environment;
        this.entities = entities;
        this.rules = rules;
        this.termination = termination;
        this.threadPoolCount = threadPoolCount;
        this.grid = grid;
        this.name = name;
        this.sleep = Optional.ofNullable(sleep);
    }

    public boolean isSleep() {
        return sleep.isPresent();
    }

    public Integer getSleep() {
        return sleep.get();
    }

    public String getName() {
        return name;
    }

    public int getThreadPoolCount() {
        return threadPoolCount;
    }

    public Grid getGrid() {
        return grid;
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
