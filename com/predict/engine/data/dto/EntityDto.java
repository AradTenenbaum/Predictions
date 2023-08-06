package com.predict.engine.data.dto;

import java.util.List;

public class EntityDto {
    private String name;
    private int population;
    private List<PropertyDto> properties;

    public EntityDto(String name, int population, List<PropertyDto> properties) {
        this.name = name;
        this.population = population;
        this.properties = properties;
    }
}
