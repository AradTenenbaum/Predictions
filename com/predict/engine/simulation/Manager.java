package com.predict.engine.simulation;

import com.predict.engine.data.dto.WorldDto;
import com.predict.engine.def.World;
import com.predict.engine.ins.EntityInstance;
import com.predict.engine.ins.PropertyInstance;
import com.predict.engine.ins.environment.EnvironmentInstance;
import com.predict.engine.utils.exception.SimulationException;

import java.util.*;

public class Manager {
    private World currentWorld;
    private WorldDto sharedWorld;
    private Boolean isValidWorld;
    private History history;

    public Manager() {
        this.isValidWorld = false;
        this.history = new History();
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

    public Simulation getSimulationById(int id) {
        return history.getSimulationById(id);
    }

    public void runSimulation(EnvironmentInstance env) throws SimulationException, RuntimeException {
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

        int ticks = 0;
        long startTime = System.currentTimeMillis();
        long duration = 1000L *currentWorld.getTermination().getSeconds();
        while (ticks < currentWorld.getTermination().getTicks() && (System.currentTimeMillis() - startTime < duration)) {
            int finalTicks = ticks;

            currentWorld.getRules().forEach(rule -> {
                if(rule.isActive(finalTicks)) {
                    rule.getActions().forEach(action -> {
                        entities.get(action.getEntity()).stream().filter(EntityInstance::getAlive).forEach(entityInstance -> {
                            try {
                                action.invoke(entityInstance, env);

                            } catch (SimulationException e) {
                                throw new RuntimeException(action.getEntity() + " -> " + rule.getName() + " -> " + e.getMessage());
                            }
                        });
                    });
                }
            });

            ticks++;
        }

        // Testing simulation
        entities.forEach(((s, entityInstances) -> {
            entityInstances.forEach(entityInstance -> {
                System.out.println(entityInstance.toString());
            });
        }));
        // Testing simulation

        Simulation s = new Simulation(entities, new Date());
        System.out.print("Simulation "+ s.getId() +" stopped: ");

        if(!(ticks < currentWorld.getTermination().getTicks())) {
            System.out.println("Passed " + currentWorld.getTermination().getTicks() + " ticks");
        } else {
            System.out.println("Passed " + currentWorld.getTermination().getSeconds() + " seconds");
        }

        history.saveSimulation(s);
        // TODO: return the results and not print them
    }
}
