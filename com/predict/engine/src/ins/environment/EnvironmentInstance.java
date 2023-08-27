package ins.environment;

import ins.PropertyInstance;

import java.util.Map;

public interface EnvironmentInstance {
    PropertyInstance getProperty(String property);
    void setProperty(String property, String value, String type);
    Map<String, PropertyInstance> getProperties();
}
