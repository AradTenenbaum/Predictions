package com.predict.engine.simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class History implements Serializable {
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

    public Simulation getSimulationById(UUID id) {
        Optional<Simulation> s = simulations.stream().filter(simulation -> simulation.getId().equals(id)).findFirst();
        if(s.isPresent()) return s.get();
        else return null;
    }
}
