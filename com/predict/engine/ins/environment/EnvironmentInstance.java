package com.predict.engine.ins.environment;

import com.predict.engine.ins.PropertyInstance;

import java.util.List;
import java.util.Map;

public interface EnvironmentInstance {
    PropertyInstance getProperty(String property);
    void setProperty(String property, String value, String type);
    Map<String, PropertyInstance> getProperties();
}
