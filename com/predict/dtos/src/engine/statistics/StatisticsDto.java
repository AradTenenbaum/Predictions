package engine.statistics;


import java.util.Map;

public class StatisticsDto {
    private Map<String, EntityStatisticsDto> entityStatisticsDto;

    public StatisticsDto() {
    }

    public StatisticsDto(Map<String, EntityStatisticsDto> entityStatisticsDto) {
        this.entityStatisticsDto = entityStatisticsDto;
    }

    public Map<String, EntityStatisticsDto> getEntityStatisticsDto() {
        return entityStatisticsDto;
    }
}
