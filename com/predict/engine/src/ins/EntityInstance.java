package ins;

import utils.object.Grid;
import utils.object.Position;

import java.io.Serializable;
import java.util.Map;
public class EntityInstance implements Serializable {
    private static int idGenerator = 1;
    private int id;
    private String name;
    private Map<String, PropertyInstance> properties;
    private Boolean isAlive;
    private Position position;
    public EntityInstance(String name, Map<String, PropertyInstance> properties) {
        this.id = idGenerator++;
        this.name = name;
        this.properties = properties;
        this.isAlive = true;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void move(Grid simulationGrid) {
        Position newPos = simulationGrid.moveToFreePosition(position);
        setPosition(newPos);
    }

    public void setProperty(String property, Object value) {
        String type = properties.get(property).getType();
        properties.put(property, new PropertyInstance(type, value));
    }

    public Object getPropertyValue(String property) {
        return properties.get(property).getValue();
    }

    public String getPropertyType(String property) {
        return properties.get(property).getType();
    }

    public Map<String, PropertyInstance> getProperties() {
        return properties;
    }

    public Boolean hasProperty(String property) {
        return properties.containsKey(property);
    }

    public int getId() {
        return id;
    }

    public void kill() {
        isAlive = false;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    @Override
    public String toString() {
        return "EntityInstance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", properties=" + properties +
                ", isAlive=" + isAlive +
                '}';
    }
}
