package ins.environment;

import ins.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentInstanceImpl implements EnvironmentInstance {
    private Map<String, PropertyInstance> properties;
    private Map<String, Integer> populations;

    public EnvironmentInstanceImpl() {
        this.properties = new HashMap<>();
        this.populations = new HashMap<>();
    }

    public EnvironmentInstanceImpl(EnvironmentInstance environmentInstance, Map<String, Integer> populations) {
        this.properties = new HashMap<>();
        environmentInstance.getProperties().forEach((s, propertyInstance) -> {
            this.properties.put(s, new PropertyInstance(propertyInstance));
        });

        this.populations = new HashMap<>();
        populations.forEach((s, integer) -> {
            this.populations.put(s, integer);
        });
    }

    @Override
    public void addRandomProperty(String name, String type) {
        properties.put(name, new PropertyInstance(type));
    }

    @Override
    public void addProperty(String name, String type, Object value) {
        properties.put(name, new PropertyInstance(type, value));
    }

    @Override
    public void setProperty(String property, Object value) {
        this.properties.get(property).setValue(value);
    }

    @Override
    public PropertyInstance getProperty(String property) {
        return this.properties.get(property);
    }

    @Override
    public boolean isRandomProp(String property) {
        return this.properties.get(property).isRandom();
    }

    @Override
    public Map<String, PropertyInstance> getProperties() {
        return properties;
    }

    @Override
    public Map<String, Integer> getPopulations() {
        return populations;
    }
}
