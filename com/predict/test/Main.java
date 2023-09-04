package com.predict.test;

import data.source.File;
import def.PropertyType;
import ins.environment.EnvironmentInstance;
import ins.environment.EnvironmentInstanceImpl;
import simulation.Manager;
import utils.func.RandomGenerator;
import utils.object.Range;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        try {
//            File.fetchDataFromFile("C:\\Users\\aradt\\IdeaProjects\\Predictions\\com\\predict\\resources\\ex2\\master-ex2-custom.xml" ,manager);
//            EnvironmentInstance env = new EnvironmentInstanceImpl();
//            env.setProperty("e1", RandomGenerator.getRandom(PropertyType.FLOAT, new Range(10, 100)), PropertyType.FLOAT);
//            env.setProperty("e2", RandomGenerator.getRandom(PropertyType.BOOLEAN, null), PropertyType.BOOLEAN);
//            env.setProperty("e3", RandomGenerator.getRandom(PropertyType.FLOAT, new Range(10.4, 100.2)), PropertyType.FLOAT);
//            env.setProperty("e4", RandomGenerator.getRandom(PropertyType.STRING, null), PropertyType.STRING);
//            Map<String, Integer> population = new HashMap<>();
//            population.put("ent-1", 10);
//            population.put("ent-2", 20);
//            manager.setPopulations(population);

//            File.fetchDataFromFile("C:\\Users\\aradt\\IdeaProjects\\Predictions\\com\\predict\\resources\\ex2\\ex2-virus-custom.xml" ,manager);
//            EnvironmentInstance env = new EnvironmentInstanceImpl();
//            env.setProperty("infection-proximity", RandomGenerator.getRandom(PropertyType.FLOAT, new Range(1, 3)), PropertyType.FLOAT);
//            Map<String, Integer> population = new HashMap<>();
//            population.put("Healthy", 5);
//            population.put("Sick", 80);
//            manager.setPopulations(population);

//            manager.runSimulation(env);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
