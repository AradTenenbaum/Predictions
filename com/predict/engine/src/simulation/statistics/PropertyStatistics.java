package simulation.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PropertyStatistics {
    private Map<Object, Integer> entAmountPerValue;
    private int sumTicksLastChange;
    private int amountOfSamplesTicksLastChange;
    private Integer avgValue;

    public PropertyStatistics() {
        this.entAmountPerValue = new HashMap<>();
        this.sumTicksLastChange = 0;
        this.amountOfSamplesTicksLastChange = 0;
        this.avgValue = null;
    }

    public void addEntAmountPerValue(Object value, int amount) {
        entAmountPerValue.put(value, amount);
    }

    public void addLastChangeTicks(int lastChangeTicks) {
        sumTicksLastChange += lastChangeTicks;
        amountOfSamplesTicksLastChange++;
    }

    public double getAverageChangeTicks() {
        return (double) sumTicksLastChange / amountOfSamplesTicksLastChange;
    }
}
