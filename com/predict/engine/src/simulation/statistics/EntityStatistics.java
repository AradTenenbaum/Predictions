package simulation.statistics;

import utils.object.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityStatistics {
    private int aliveAmount;
    private int deadAmount;
    private List<Point> amountInTimeline;
    private Map<String, PropertyStatistics> propertyStatistics;

    public EntityStatistics() {
        propertyStatistics = new HashMap<>();
        amountInTimeline = new ArrayList<>();
        aliveAmount = 0;
        deadAmount = 0;
    }

    public void addPoint(Point point) {
        amountInTimeline.add(point);
    }

    public void addProperty(String property) {
        propertyStatistics.put(property, new PropertyStatistics());
    }

    public Map<String, PropertyStatistics> getPropertyStatistics() {
        return propertyStatistics;
    }

    public List<Point> getAmountInTimeline() {
        return amountInTimeline;
    }

    public PropertyStatistics getProperty(String property) {
        return propertyStatistics.get(property);
    }

    public void setAliveAmount(int aliveAmount) {
        this.aliveAmount = aliveAmount;
    }

    public void setDeadAmount(int deadAmount) {
        this.deadAmount = deadAmount;
    }
}
