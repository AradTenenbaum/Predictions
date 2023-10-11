package engine;

import java.util.Map;

public class CreateSimulationDto {
    int requestId;
    Map<String, String> environment;
    Map<String, Integer> populations;

    public CreateSimulationDto(Map<String, String> environment, Map<String, Integer> populations, int requestId) {
        this.environment = environment;
        this.populations = populations;
        this.requestId = requestId;
    }

    public CreateSimulationDto() {
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public Map<String, Integer> getPopulations() {
        return populations;
    }

    public int getRequestId() {
        return requestId;
    }
}
