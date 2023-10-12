package engine.statistics;

import java.util.Map;

public class PropertyStatisticsDto {
    private Map<Object, Integer> entAmountPerValue;
    private double avgValue;
    private double avgChangeTicks;

    private boolean isNumeric;

    public PropertyStatisticsDto(Map<Object, Integer> entAmountPerValue, double avgValue, double avgChangeTicks, boolean isNumeric) {
        this.entAmountPerValue = entAmountPerValue;
        this.avgValue = avgValue;
        this.avgChangeTicks = avgChangeTicks;
        this.isNumeric = isNumeric;
    }

    public Map<Object, Integer> getEntAmountPerValue() {
        return entAmountPerValue;
    }

    public double getAvgValue() {
        return avgValue;
    }

    public double getAvgChangeTicks() {
        return avgChangeTicks;
    }

    public boolean isNumeric() {
        return isNumeric;
    }
}
