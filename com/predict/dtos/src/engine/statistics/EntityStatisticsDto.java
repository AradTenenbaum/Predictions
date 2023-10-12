package engine.statistics;

import generic.objects.Point;

import java.util.List;
import java.util.Map;

public class EntityStatisticsDto {
    private int aliveAmount;
    private int deadAmount;
    private List<Point> amountInTimeline;
    private Map<String, PropertyStatisticsDto> propertiesStatisticsDto;

    public EntityStatisticsDto(int aliveAmount, int deadAmount, List<Point> amountInTimeline, Map<String, PropertyStatisticsDto> propertiesStatisticsDto) {
        this.aliveAmount = aliveAmount;
        this.deadAmount = deadAmount;
        this.amountInTimeline = amountInTimeline;
        this.propertiesStatisticsDto = propertiesStatisticsDto;
    }

    public int getAliveAmount() {
        return aliveAmount;
    }

    public int getDeadAmount() {
        return deadAmount;
    }

    public List<Point> getAmountInTimeline() {
        return amountInTimeline;
    }

    public Map<String, PropertyStatisticsDto> getPropertiesStatisticsDto() {
        return propertiesStatisticsDto;
    }
}
