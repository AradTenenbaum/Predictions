package data.dto;

import java.io.Serializable;
import java.util.List;

public class EntityDto implements Serializable {
    private String name;
    private int population;
    private List<PropertyDto> properties;

    public EntityDto(String name, int population, List<PropertyDto> properties) {
        this.name = name;
        this.population = population;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public List<PropertyDto> getProperties() {
        return properties;
    }
}
