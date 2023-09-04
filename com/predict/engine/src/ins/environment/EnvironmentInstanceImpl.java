package ins.environment;

import ins.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentInstanceImpl implements EnvironmentInstance {
    private Map<String, PropertyInstance> properties;

    public EnvironmentInstanceImpl() {
        this.properties = new HashMap<>();
    }

    @Override
    public void addRandomProperty(String name, String type) {
        properties.put(name, new PropertyInstance(type));
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
}
