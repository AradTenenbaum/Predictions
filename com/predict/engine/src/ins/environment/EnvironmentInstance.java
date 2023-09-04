package ins.environment;

import ins.PropertyInstance;

import java.util.Map;

public interface EnvironmentInstance {
    PropertyInstance getProperty(String property);
    public void setProperty(String property, Object value);
    Map<String, PropertyInstance> getProperties();
    public void addRandomProperty(String name, String type);
    public boolean isRandomProp(String property);
}
