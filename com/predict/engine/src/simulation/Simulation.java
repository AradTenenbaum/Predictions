package simulation;

import data.dto.WorldDto;
import def.PropertyType;
import def.Termination;
import def.World;
import ins.EntityInstance;
import ins.environment.EnvironmentInstance;
import ins.environment.EnvironmentInstanceImpl;
import simulation.statistics.Statistics;
import utils.exception.SimulationException;
import utils.func.RandomGenerator;
import utils.object.Grid;
import utils.object.Point;
import utils.object.Range;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Simulation implements Serializable {
    private UUID id;
    private Map<String, List<EntityInstance>> entities;
    private Date runDate;
    private WorldDto worldDto;
    private World world;
    private Termination.REASONS terminationReason;
    private Grid grid;
    private EnvironmentInstance environmentInstance;
    private STATUS status;
    private long runTime;
    private int ticks;
    private Statistics statistics;
    private int worldVersion;
    public enum STATUS {
        RUN, PAUSED, STOPPED
    }

    public Simulation(Map<String, List<EntityInstance>> entities, WorldDto worldDto, World world, EnvironmentInstance environmentInstance, Grid grid, int worldVersion) {
        this.id = UUID.randomUUID();
        this.entities = entities;
        this.runDate = new Date();
        this.worldDto = worldDto;
        this.world = world;
        Map<String, Integer> populations = new HashMap<>();
        this.statistics = new Statistics();
        world.getEntities().forEach((s, entity) -> {
            populations.put(s, entity.getPopulation());
            statistics.addEntity(s);
            entity.getProperties().forEach((s1, property) -> {
                statistics.getEntity(s).addProperty(s1);
            });
            statistics.addPointToGraph(s, new Point(0, entity.getPopulation()));
        });
        this.environmentInstance = new EnvironmentInstanceImpl(environmentInstance, populations);
        this.status = STATUS.RUN;
        this.grid = grid;
        this.runTime = 0;
        this.ticks = 0;
        this.worldVersion = worldVersion;
    }

    public void pause() {
        if(this.status != STATUS.STOPPED) {
            this.status = STATUS.PAUSED;
        }
    }

    public void resume() {
        if(this.status != STATUS.STOPPED) {
            this.status = STATUS.RUN;
        }
    }

    public int getWorldVersion() {
        return worldVersion;
    }

    public Map<String, Integer> getPopulations() {
        return environmentInstance.getPopulations();
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public boolean isUserStop() {
        return world.getTermination().isByUser();
    }

    public void stop() {
        this.status = STATUS.STOPPED;
        entities.forEach((s, entityInstances) -> {
            statistics.setEntityAliveAmount(s, (int) entityInstances.stream().filter(EntityInstance::getAlive).count());
            statistics.setEntityDeadAmount(s, (int) entityInstances.stream().filter(entityInstance -> !entityInstance.getAlive()).count());

            statistics.getEntity(s).getPropertyStatistics().forEach((s1, propertyStatistics) -> {
                Map<Object, List<EntityInstance>> groupBy = entityInstances.stream().filter(EntityInstance::getAlive).collect(Collectors.groupingBy(entityInstance -> entityInstance.getPropertyValue(s1)));
                groupBy.forEach((o, entityInstances1) -> {
                    propertyStatistics.addEntAmountPerValue(o, entityInstances1.size());
                });
            });

            entityInstances.forEach(entityInstance -> entityInstance.getProperties().forEach((propertyName, propertyInstance) -> {
                if(PropertyType.isTypeNumber(propertyInstance.getType())) {
                    Double value = (propertyInstance.getValue() instanceof Integer ? ((Integer) propertyInstance.getValue()).doubleValue() : (Double) propertyInstance.getValue());
                    statistics.getEntity(s).getPropertyStatistics().get(propertyName).addValue(value);
                }
            }));
        });
    }

    public void checkStop() {
        Termination termination = world.getTermination();
        long duration = 1000L *termination.getSeconds();
        if(termination.getTicks() != -1 && ticks > termination.getTicks()) {
            stop();
        }
        else if(termination.getSeconds() != -1 && runTime > duration) {
            stop();
        }
    }

    public double getProgress() {
        if(isStopped()) return 1;
        Termination termination = world.getTermination();
        if(termination.getTicks() != -1) {
            return ((double) ticks /termination.getTicks());
        }
        else if(termination.getSeconds() != -1) {
            return ((double) getRunTime() /termination.getSeconds());
        }
        return 0;
    }

    public boolean isStopped() {return this.status==STATUS.STOPPED;}
    public boolean isPaused() {return this.status==STATUS.PAUSED;}
    public boolean isRun() {return this.status==STATUS.RUN;}

    public long getRunTime() {
        return (runTime/1000L);
    }

    public int getTicks() {
        return ticks;
    }

    public long getAliveEntities() {
        AtomicLong num = new AtomicLong();
        entities.forEach((s, entityInstances) -> {
            num.addAndGet(entityInstances.stream().filter(EntityInstance::getAlive).count());
        });
        return num.get();
    }

    public EnvironmentInstance getEnvironmentInstance() {
        return environmentInstance;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void runTick() {
        if(isRun()) {
            long startTime = System.currentTimeMillis();
            List<EntityInstance> toCreate = new ArrayList<>();

            int finalTicks = ticks;
            world.getRules().forEach(rule -> {
                if(rule.isActive(finalTicks)) {
                    rule.getActions().forEach(action -> {
                        entities.get(action.getEntity()).stream().filter(EntityInstance::getAlive).forEach(entityInstance -> {
                            try {
                                if(entityInstance.getAlive()) {
                                    if(action.getSecondaryEntity().isPresent()) {
                                        List<EntityInstance> secondaries = entities.get(action.getSecondaryEntity().get().getName())
                                                .stream()
                                                .filter(secEntityInstance -> {
                                                    if(action.getSecondaryEntity().get().getCondition().isPresent()) {
                                                        try {
                                                            return action.getSecondaryEntity().get().getCondition().get().isTrue(secEntityInstance, environmentInstance, null);
                                                        } catch (SimulationException e) {
                                                            throw new RuntimeException("An error occurred");
                                                        }
                                                    }
                                                    return true;
                                                }).collect(Collectors.toList());
                                        if(action.getSecondaryEntity().get().isAll()) {
                                            secondaries.forEach(secEntity -> {
                                                try {
                                                    action.invoke(new InvokeKit(entityInstance, environmentInstance, entities, world, grid, toCreate, finalTicks, statistics, new Context(secEntity)));
                                                } catch (SimulationException e) {
                                                    throw new RuntimeException("An error occurred");
                                                }
                                            });
                                        } else {
                                            if(secondaries.size() > 0) {
                                                for(int i = 0; i < action.getSecondaryEntity().get().getCount(); i++) {
                                                    EntityInstance randomSecEntity = secondaries.get(RandomGenerator.getInt(new Range(0, secondaries.size()-1)));
                                                    try {
                                                        action.invoke(new InvokeKit(entityInstance, environmentInstance, entities, world, grid, toCreate, finalTicks, statistics, new Context(randomSecEntity)));
                                                    } catch (SimulationException e) {
                                                        throw new RuntimeException("An error occurred");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        action.invoke(new InvokeKit(entityInstance, environmentInstance, entities, world, grid, toCreate, finalTicks, statistics));
                                    }
                                }
                            } catch (SimulationException e) {
                                throw new RuntimeException(action.getEntity() + " -> " + rule.getName() + " -> " + e.getMessage());
                            }
                        });
                    });
                }
            });

            toCreate.forEach(entityInstance -> {
                entities.get(entityInstance.getName()).add(entityInstance);
            });
            toCreate.clear();

            // move all entities
            entities.forEach(((entityName, entityInstances) -> {
                entityInstances.forEach(entityInstance -> {
                    if(entityInstance.getAlive()) entityInstance.move(grid);
                    else {
                        if(grid.getPos(entityInstance.getPosition()) == entityInstance.getId()) grid.removeFromPos(entityInstance.getPosition());
                    }
                });
            }));
            ticks++;
            runTime += (System.currentTimeMillis()-startTime);

            // Update statistics
            entities.forEach((s, entityInstances) -> {
                statistics.addPointToGraph(s, new Point(ticks, (int) entityInstances.stream().filter(EntityInstance::getAlive).count()));
            });

            checkStop();
        }
    }


    public UUID getId() {
        return id;
    }

    public Map<String, List<EntityInstance>> getEntities() {
        return entities;
    }

    public Termination.REASONS getTerminationReason() {
        return terminationReason;
    }

    public WorldDto getWorldDto() {
        return worldDto;
    }

    public void setTerminationReason(Termination.REASONS terminationReason) {
        this.terminationReason = terminationReason;
    }

    public String getFormattedRunDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy | HH.mm.ss");
        return formatter.format(runDate);
    }
}
