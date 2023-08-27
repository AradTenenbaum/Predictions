package com.predict.test;

import data.source.File;
import def.PropertyType;
import ins.environment.EnvironmentInstance;
import ins.environment.EnvironmentInstanceImpl;
import simulation.Manager;
import utils.func.RandomGenerator;
import utils.object.Range;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        try {
            File.fetchDataFromFile("C:\\Users\\aradt\\IdeaProjects\\Predictions\\com\\predict\\resources\\ex2\\master-ex2-custom.xml" ,manager);

            EnvironmentInstance env = new EnvironmentInstanceImpl();
            env.setProperty("e1", RandomGenerator.getRandom(PropertyType.FLOAT, new Range(10, 100)), PropertyType.FLOAT);
            env.setProperty("e2", RandomGenerator.getRandom(PropertyType.BOOLEAN, null), PropertyType.BOOLEAN);
            env.setProperty("e3", RandomGenerator.getRandom(PropertyType.FLOAT, new Range(10.4, 100.2)), PropertyType.FLOAT);
            env.setProperty("e4", RandomGenerator.getRandom(PropertyType.STRING, null), PropertyType.STRING);
            manager.runSimulation(env);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
