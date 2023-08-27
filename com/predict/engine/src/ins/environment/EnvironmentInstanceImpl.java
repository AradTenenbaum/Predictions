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
    public void setProperty(String property, String value, String type) {
        this.properties.put(property, new PropertyInstance(type, value));
    }

    @Override
    public PropertyInstance getProperty(String property) {
        return this.properties.get(property);
    }

    @Override
    public Map<String, PropertyInstance> getProperties() {
        return properties;
    }
}
