package simulation;

import data.dto.WorldDto;
import data.validation.Validation;
import def.Termination;
import def.World;
import ins.EntityInstance;
import ins.PropertyInstance;
import ins.environment.EnvironmentInstance;
import ins.environment.EnvironmentInstanceImpl;
import utils.exception.SimulationException;
import utils.exception.ValidationException;
import utils.func.Convert;
import utils.func.RandomGenerator;
import utils.object.Grid;
import utils.object.Range;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Manager implements Serializable {
    private World currentWorld;
    private WorldDto sharedWorld;
    private Boolean isValidWorld;
    private History history;
    private EnvironmentInstance environmentInstance;

    public Manager() {
        this.isValidWorld = false;
        this.history = new History();
        this.environmentInstance = new EnvironmentInstanceImpl();
    }

    public void setEnvVar(String property, String value) {
        Object fixedValue = value;
        try {
            Validation.checkPropValid(currentWorld.getEnvironment().getProperties().get(property), value);
            fixedValue = Convert.stringToType(value, currentWorld.getEnvironment().getProperties().get(property).getType());
            environmentInstance.setProperty(property, fixedValue);
        } catch (ValidationException e) {
            System.out.println("Not valid");
        }
    }

    public boolean isRandomEnvVar(String name) {
        return environmentInstance.isRandomProp(name);
    }

    public String getEnvValue(String property) {
        return environmentInstance.getProperty(property).getValue().toString();
    }

    public void setPopulations(Map<String, Integer> populations) {
        currentWorld.getEntities().forEach((s, entity) -> {
            if(populations.containsKey(s)) {
                entity.setPopulation(populations.get(s));
            }
        });
    }

    public void setPopulation(String entity, int number) {
        currentWorld.getEntities().get(entity).setPopulation(number);
        sharedWorld = new WorldDto(currentWorld);
    }

    public void setCurrentWorld(World currentWorld) {
        this.currentWorld = currentWorld;
        clearEnv();
    }

    public void clearEnv() {
        if(currentWorld != null) {
            currentWorld.getEnvironment().getProperties().forEach((s, property) -> {
                environmentInstance.addRandomProperty(s, property.getType());
            });
            currentWorld.getEntities().forEach((s, entity) -> {
                entity.setPopulation(0);
            });
            sharedWorld = new WorldDto(currentWorld);
        }
    }

    public void setSharedWorld(WorldDto sharedWorld) {
        this.sharedWorld = sharedWorld;
    }

    public void setValidWorld(Boolean validWorld) {
        isValidWorld = validWorld;
    }

    public WorldDto getSharedWorld() {
        return sharedWorld;
    }

    public Boolean getValidWorld() {
        return isValidWorld;
    }

    public List<Simulation> getSimulationsHistory() {
        return history.getSimulations();
    }

    public Simulation getSimulationById(UUID id) {
        return history.getSimulationById(id);
    }

    public Simulation runSimulation() throws SimulationException, RuntimeException {
        EnvironmentInstance env = environmentInstance;
        sharedWorld.getEnvironment().getProperties().forEach(propertyDto -> {
            if(env.getProperty(propertyDto.getName()).isRandom()) {
                Object value = RandomGenerator.getRandom(propertyDto.getType(), propertyDto.getRange());
                env.setProperty(propertyDto.getName(), value);
            }
        });

        if(currentWorld == null && !isValidWorld) {
            throw new SimulationException("no valid file was loaded. please load a file to run this action");
        }

        // Generate a map of entity name to list of instances
        Map<String, List<EntityInstance>> entities = new HashMap<>(currentWorld.getEntities().size());
        currentWorld.getEntities().forEach((s, entity) -> {
            entities.put(s, new ArrayList<>(entity.getPopulation()));
        });

        // Generate instances for each entity
        entities.forEach((entityName, entityInstances) -> {
            for (int i = 0; i < currentWorld.getEntities().get(entityName).getPopulation(); i++) {
                Map<String, PropertyInstance> properties = new HashMap<>();
                currentWorld.getEntities().get(entityName).getProperties().forEach((propertyName, property) -> {
                    properties.put(propertyName, new PropertyInstance(property.getType(), property.generateValue()));
                });
                entityInstances.add(new EntityInstance(entityName, properties));
            }
        });

        // Create the grid
        Grid grid = currentWorld.getGrid().generateGrid(entities);

        List<EntityInstance> toCreate = new ArrayList<>();

        int ticks = 0;
        long startTime = System.currentTimeMillis();
        long duration = 1000L *currentWorld.getTermination().getSeconds();
        while (ticks < currentWorld.getTermination().getTicks() && (System.currentTimeMillis() - startTime < duration)) {

            int finalTicks = ticks;
            // TODO: check if the condition action needs to work without entity
            currentWorld.getRules().forEach(rule -> {
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
                                                            return action.getSecondaryEntity().get().getCondition().get().isTrue(secEntityInstance, env, null);
                                                        } catch (SimulationException e) {
                                                            throw new RuntimeException("An error occurred");
                                                        }
                                                    }
                                                    return true;
                                                }).collect(Collectors.toList());
                                        if(action.getSecondaryEntity().get().isAll()) {
                                            secondaries.forEach(secEntity -> {
                                                try {
                                                    action.invoke(new InvokeKit(entityInstance, env, entities, currentWorld, grid, toCreate, finalTicks, new Context(secEntity)));
                                                } catch (SimulationException e) {
                                                    throw new RuntimeException("An error occurred");
                                                }
                                            });
                                        } else {
                                            if(secondaries.size() > 0) {
                                                for(int i = 0; i < action.getSecondaryEntity().get().getCount(); i++) {
                                                    EntityInstance randomSecEntity = secondaries.get(RandomGenerator.getInt(new Range(0, secondaries.size()-1)));
                                                    try {
                                                        action.invoke(new InvokeKit(entityInstance, env, entities, currentWorld, grid, toCreate, finalTicks, new Context(randomSecEntity)));
                                                    } catch (SimulationException e) {
                                                        throw new RuntimeException("An error occurred");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        action.invoke(new InvokeKit(entityInstance, env, entities, currentWorld, grid, toCreate, finalTicks));
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
            entities.forEach(((s, entityInstances) -> {
                entityInstances.forEach(entityInstance -> {
                    if(entityInstance.getAlive()) entityInstance.move(grid);
                    else {
                        if(grid.getPos(entityInstance.getPosition()) == entityInstance.getId()) grid.removeFromPos(entityInstance.getPosition());
                    }
                });
            }));
            ticks++;
        }

        // Testing simulation
        entities.forEach(((s, entityInstances) -> {
            System.out.println(s + ": " + entityInstances.stream().filter(EntityInstance::getAlive).count());
            entityInstances.forEach(entityInstance -> {
                System.out.println(entityInstance.toString());
            });
        }));
        // Testing simulation

        Simulation s = new Simulation(entities, sharedWorld);

        if(!(ticks < currentWorld.getTermination().getTicks())) {
            s.setTerminationReason(Termination.REASONS.TICKS);
        } else {
            s.setTerminationReason(Termination.REASONS.SECONDS);
        }

        history.saveSimulation(s);

        clearEnv();
        return s;
    }
}
