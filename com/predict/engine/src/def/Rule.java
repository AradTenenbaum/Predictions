package def;

import def.action.Action;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Rule implements Serializable {
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

    public List<Action> getActions() {
        return actions;
    }

    public String getName() {
        return name;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }

    public Boolean isActive(int simulationTicks) {
        Random random = new Random();
        double randomValue = random.nextDouble();
        return ((randomValue < probability) && (simulationTicks > 0 && simulationTicks % ticks == 0));
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
