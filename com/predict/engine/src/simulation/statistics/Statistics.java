package simulation.statistics;

import generic.objects.Point;
import utils.object.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    private Map<String, EntityStatistics> entityStatistics;

    public Statistics() {
        entityStatistics = new HashMap<>();
    }

    public void addPointToGraph(String entity, Point point) {
        entityStatistics.get(entity).addPoint(point);
    }

    public void addEntity(String entity) {
        entityStatistics.put(entity, new EntityStatistics());
    }

    public EntityStatistics getEntity(String entity) {
        return entityStatistics.get(entity);
    }

    public Map<String, EntityStatistics> getEntityStatistics() {
        return entityStatistics;
    }

    public void setEntityAliveAmount(String entity, int amount) {
        entityStatistics.get(entity).setAliveAmount(amount);
    }

    public void setEntityDeadAmount(String entity, int amount) {
        entityStatistics.get(entity).setDeadAmount(amount);
    }
}
