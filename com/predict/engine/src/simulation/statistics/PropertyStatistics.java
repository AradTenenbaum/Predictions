package simulation.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PropertyStatistics {
    private Map<Object, Integer> entAmountPerValue;
    private int sumTicksLastChange;
    private int amountOfSamplesTicksLastChange;
    private Double sumValue;
    private int amountOfSamplesValue;
    private boolean isNumeric;

    public PropertyStatistics() {
        this.entAmountPerValue = new HashMap<>();
        this.sumTicksLastChange = 0;
        this.amountOfSamplesTicksLastChange = 0;
        this.sumValue = 0.0;
        this.amountOfSamplesValue = 0;
        this.isNumeric = false;
    }

    public void addValue(Double value) {
        sumValue += value;
        amountOfSamplesValue++;
        if(!isNumeric) isNumeric=true;
    }

    public double getAverageValue() {
        if(amountOfSamplesValue == 0) return 0;
        double value = sumValue / amountOfSamplesValue;
        return value;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public void addEntAmountPerValue(Object value, int amount) {
        entAmountPerValue.put(value, amount);
    }

    public void addLastChangeTicks(int lastChangeTicks) {
        sumTicksLastChange += lastChangeTicks;
        amountOfSamplesTicksLastChange++;
    }

    public Map<Object, Integer> getEntAmountPerValue() {
        return entAmountPerValue;
    }

    public double getAverageChangeTicks() {
        if(amountOfSamplesTicksLastChange == 0) return 0;
        double value = (double) sumTicksLastChange / amountOfSamplesTicksLastChange;
        return value;
    }
}
