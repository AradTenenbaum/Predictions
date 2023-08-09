package com.predict.engine.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class History {
    private List<Simulation> simulations;

    public History() {
        this.simulations = new ArrayList<>();
    }

    public void saveSimulation(Simulation s) {
        simulations.add(s);
    }

    public List<Simulation> getSimulations() {
        return simulations;
    }

    public Simulation getSimulationById(int id) {
        Optional<Simulation> s = simulations.stream().filter(simulation -> simulation.getId() == id).findFirst();
        if(s.isPresent()) return s.get();
        else return null;
    }
}
