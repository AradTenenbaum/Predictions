package com.predict.console;

import com.predict.engine.data.dto.EntityDto;
import com.predict.engine.data.dto.EnvironmentDto;
import com.predict.engine.data.dto.WorldDto;
import com.predict.engine.data.source.File;
import com.predict.engine.data.validation.Validation;
import com.predict.engine.def.PropertyType;
import com.predict.engine.def.Termination;
import com.predict.engine.ins.EntityInstance;
import com.predict.engine.ins.environment.EnvironmentInstance;
import com.predict.engine.ins.environment.EnvironmentInstanceImpl;
import com.predict.engine.simulation.Manager;
import com.predict.engine.simulation.Simulation;
import com.predict.engine.utils.exception.FileException;
import com.predict.engine.utils.exception.SimulationException;
import com.predict.engine.utils.exception.ValidationException;
import com.predict.engine.utils.func.RandomGenerator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Menu {
    // TODO: validate user input
    private Manager manager;

    public Menu(Manager manager) {
        this.manager = manager;
    }

    public void displayMainMenu() {
        int i = 1;
        System.out.println("\n----------------------");
        System.out.println("Welcome to Predictions");
        System.out.println("----------------------");
        System.out.println("What would you like to do?");
        System.out.println((i++) + ". Load a file (xml)");
        System.out.println((i++) + ". Display simulation");
        System.out.println((i++) + ". Run a simulation");
        System.out.println((i++) + ". History");
        System.out.println((i) + ". exit");
    }

    public void choose() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Your choice: ");
        int choice = scanner.nextInt();
        if(choice == 1) {
            loadXML();
        } else if(choice == 2) {
            displaySimulation();
        } else if (choice == 3) {
            if(!manager.getValidWorld()) System.out.println("No valid file was loaded. try to load a file");
            else {
                try {
                    EnvironmentInstance env = createEnvironment(manager.getSharedWorld().getEnvironment());
                    Simulation s = manager.runSimulation(env);

                    System.out.println("Ended simulation " + s.getId());

                    if(s.getTerminationReason() == Termination.REASONS.TICKS) {
                        System.out.println("Passed " + manager.getSharedWorld().getTermination().getTicks() + " ticks");
                    } else if(s.getTerminationReason() == Termination.REASONS.SECONDS) {
                        System.out.println("Passed " + manager.getSharedWorld().getTermination().getSeconds() + " seconds");
                    }

                } catch (RuntimeException | SimulationException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (choice == 4) {
            historyMenu();
        } else if(choice == 5) {
            System.out.println("Thank you and Goodbye!");
            System.exit(0);
        }
    }

    private void loadXML() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Path to the xml file: ");
        String path = scanner.nextLine();
        try {
            // test files is in "com/predict/resources/ex1-cigarets.xml" and "com/predict/resources/master-ex1.xml"
            File.fetchDataFromFile(path, manager);
        } catch (ValidationException | FileException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void historyMenu() {
        Scanner scanner = new Scanner(System.in);
        int chosenSimulationId = 1;
        while (chosenSimulationId != 0) {
            System.out.println("Simulation History:");
            System.out.println("----------------------");
            manager.getSimulationsHistory().forEach(simulation -> {
                System.out.println("Date: " + simulation.getFormattedRunDate() + " Simulation: " + simulation.getId());
                System.out.println("----------------------");
            });
            System.out.print("Choose a number of simulation to display or 0 to go back: ");
            chosenSimulationId = scanner.nextInt();
            if(chosenSimulationId != 0) {
                Simulation chosenSimulation = manager.getSimulationById(chosenSimulationId);
                if(chosenSimulation == null) System.out.println("No such simulation, please pick an existing simulation");
                else {
                    int simulationDisplayChoice = 1;
                    while(simulationDisplayChoice != 0) {
                        System.out.println("What would you like to see about the simulation:");
                        System.out.println("1. Entities amounts");
                        System.out.println("2. Property value data");
                        System.out.print("Choose between the options or 0 to go back: ");
                        simulationDisplayChoice = scanner.nextInt();
                        if(simulationDisplayChoice == 1) {
                            chosenSimulation.getEntities().forEach((s, entityInstances) -> {
                                Optional<EntityDto> entity = chosenSimulation.getWorldDto().
                                        getEntities().
                                        stream().
                                        filter(entityDto -> entityDto.getName().equals(s)).
                                        findFirst();
                                long aliveAmount = chosenSimulation.getEntities().get(s)
                                        .stream()
                                        .filter(EntityInstance::getAlive)
                                        .count();

                                entity.ifPresent(entityDto -> System.out.println("Entity: " + s + " amount before run: " + entityDto.getPopulation() + " amount after run: " + aliveAmount));
                            });
                        } else if (simulationDisplayChoice == 2) {
                            // TODO: The property group by value
                            System.out.println("Choose Entity:");
                            AtomicInteger i = new AtomicInteger();
                            chosenSimulation.getEntities().forEach((s, entityInstances) -> {
                                System.out.println((i.incrementAndGet()) + ". " + s);
                            });

                            scanner.nextLine();
                            System.out.print("entity: ");
                            String entityChosen = scanner.nextLine();

                            if(chosenSimulation.getEntities().containsKey(entityChosen)) {
                                List<EntityInstance> entityInstances = chosenSimulation.getEntities().get(entityChosen);
                                System.out.println("Choose Property:");
                                AtomicInteger j = new AtomicInteger();
                                entityInstances.get(0).getProperties().forEach((s, propertyInstance) -> {
                                    System.out.println(j.incrementAndGet() + ". " + s);
                                });
                                System.out.print("property: ");
                                String propertyChosen = scanner.nextLine();
                                if(entityInstances.get(0).hasProperty(propertyChosen)) {
                                    Map<Object, List<EntityInstance>> entitiesByProperty = entityInstances.stream()
                                            .collect(Collectors.groupingBy(entityInstance -> entityInstance.getPropertyValue(propertyChosen)));
                                    entitiesByProperty.forEach((value, entities) -> {
                                        System.out.println(propertyChosen + ": " + value + " amount: " + entities.stream().filter(EntityInstance::getAlive).count());
                                    });
                                } else {
                                    System.out.println("Property does not exists");
                                }
                            } else {
                                System.out.println("Entity does not exists");
                            }
                        }
                    }

                }
            }
        }
    }

    private void displaySimulation() {
        WorldDto sharedWorld = manager.getSharedWorld();
        if(sharedWorld != null && manager.getValidWorld()) {
            System.out.println("Entities:");
            sharedWorld.getEntities().forEach(entityDto -> {
                System.out.println(entityDto.getName());
                System.out.println("---------");
                System.out.println("population: " + entityDto.getPopulation());
                System.out.println("Properties:");
                entityDto.getProperties().forEach(propertyDto -> {
                    System.out.println("name: " + propertyDto.getName());
                    System.out.println("type: " + propertyDto.getType());
                    System.out.print(propertyDto.getRange() != null ? "range: from=" + propertyDto.getRange().getFrom() + " to=" + propertyDto.getRange().getTo() + "\n" : "");
                    System.out.print((propertyDto.getRandom() != null ? "random initialization\n" : ""));
                    System.out.println("");
                });
            });
            System.out.println("\nRules:");
            sharedWorld.getRules().forEach(ruleDto -> {
                System.out.println(ruleDto.getName());
                System.out.println("---------");
                System.out.println("probability: " + ruleDto.getProbability());
                System.out.println("ticks: " + ruleDto.getTicks());
                System.out.println("actions amount: " + ruleDto.getActions().size());
                ruleDto.getActions().forEach(System.out::println);
                System.out.println("");
            });

            System.out.println("\nTermination:");
            System.out.println("end after " + sharedWorld.getTermination().getSeconds() + " seconds or " + sharedWorld.getTermination().getTicks() + " ticks");

        }
        else {
            System.out.println("No valid file was loaded. try to load a file");
        }
    }

    private EnvironmentInstance createEnvironment(EnvironmentDto environment) throws RuntimeException {
        EnvironmentInstance envInstance = new EnvironmentInstanceImpl();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Create environment");
        environment.getProperties().forEach(propertyDto -> {
            System.out.println("property: " + propertyDto.getName() + ", type: " + propertyDto.getType() + (propertyDto.getRange() != null ? ", range: " + propertyDto.getRange().toString() : ""));
            System.out.print("Insert a value(or press enter for random value): ");
            String value = scanner.nextLine();

            if(value.equals("")) {
                value = RandomGenerator.getRandom(propertyDto.getType(), propertyDto.getRange());
            } else {
                Boolean isValid = false;

                while (!isValid) {
                    try {
                        Validation.isValueFromType(propertyDto.getType(), value);
                        if(propertyDto.getType().equals(PropertyType.DECIMAL) || propertyDto.getType().equals(PropertyType.FLOAT)) {
                            if(propertyDto.getRange() != null) {
                                Validation.isValidByRange(value, propertyDto.getRange().getFrom(), propertyDto.getRange().getTo());
                            }
                        }
                        isValid = true;
                    } catch (ValidationException e) {
                        System.out.println(e.getMessage());
                        System.out.println("property: " + propertyDto.getName() + ", type: " + propertyDto.getType() + (propertyDto.getRange() != null ? ", range: " + propertyDto.getRange().toString() : ""));
                        System.out.print("Insert a value(or press enter for random value): ");
                        value = scanner.nextLine();
                    }

                }
            }
            envInstance.setProperty(propertyDto.getName(), value, propertyDto.getType());
        });

        envInstance.getProperties().forEach((s, propertyInstance) -> System.out.println(s + "=" + propertyInstance.getValue()));

        return envInstance;
    }
}
