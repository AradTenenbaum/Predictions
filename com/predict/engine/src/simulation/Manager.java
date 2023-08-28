package simulation;

import data.dto.WorldDto;
import def.Termination;
import def.World;
import ins.EntityInstance;
import ins.PropertyInstance;
import ins.environment.EnvironmentInstance;
import utils.exception.SimulationException;
import utils.object.Grid;

import java.io.Serializable;
import java.util.*;

public class Manager implements Serializable {
    private World currentWorld;
    private WorldDto sharedWorld;
    private Boolean isValidWorld;
    private History history;

    public Manager() {
        this.isValidWorld = false;
        this.history = new History();
    }

    public void setPopulations(Map<String, Integer> populations) {
        currentWorld.getEntities().forEach((s, entity) -> {
            if(populations.containsKey(s)) {
                entity.setPopulation(populations.get(s));
            }
        });
    }

    public void setCurrentWorld(World currentWorld) {
        this.currentWorld = currentWorld;
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

    public Simulation runSimulation(EnvironmentInstance env) throws SimulationException, RuntimeException {
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
                                if(entityInstance.getAlive()) action.invoke(new InvokeKit(entityInstance, env, entities, currentWorld, grid, toCreate, finalTicks));
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

        return s;
    }
}
