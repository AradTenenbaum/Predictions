package engine;

import engine.statistics.StatisticsDto;
import simulation.Simulation;
import simulation.statistics.Statistics;

import java.util.Map;
import java.util.UUID;

public class SimulationDto {
    private UUID id;
    public enum MODES {
        RUNTIME, STATS
    }
    private int runTimeSeconds;
    private int ticks;
    private double progress;
    private Map<String, Integer> populations;
    private MODES mode;
    private StatisticsDto statistics;
    private TerminationDto terminationDto;

    public SimulationDto(UUID id, int runTimeSeconds, int ticks, double progress, Map<String, Integer> populations, MODES mode, StatisticsDto statistics, TerminationDto terminationDto) {
        this.id = id;
        this.runTimeSeconds = runTimeSeconds;
        this.ticks = ticks;
        this.progress = progress;
        this.populations = populations;
        this.mode = mode;
        this.statistics = statistics;
        this.terminationDto = terminationDto;
    }

    public UUID getId() {
        return id;
    }

    public SimulationDto() {
    }

    public int getRunTimeSeconds() {
        return runTimeSeconds;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProgress() {
        return progress;
    }

    public Map<String, Integer> getPopulations() {
        return populations;
    }

    public boolean isOnRuntimeMode() {
        return mode == MODES.RUNTIME;
    }
    public boolean isOnStatsMode() {
        return mode == MODES.STATS;
    }
    public StatisticsDto getStatistics() {
        return statistics;
    }

    public TerminationDto getTerminationDto() {
        return terminationDto;
    }
}
