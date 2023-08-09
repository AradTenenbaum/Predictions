package com.predict.engine.data.dto;

import java.util.List;

public class EnvironmentDto {
    private List<PropertyDto> properties;

    public EnvironmentDto(List<PropertyDto> properties) {
        this.properties = properties;
    }

    public List<PropertyDto> getProperties() {
        return properties;
    }
}
