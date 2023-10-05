package engine;

import java.io.Serializable;
import java.util.List;

public class EnvironmentDto implements Serializable {
    private List<PropertyDto> properties;

    public EnvironmentDto(List<PropertyDto> properties) {
        this.properties = properties;
    }

    public List<PropertyDto> getProperties() {
        return properties;
    }
}
