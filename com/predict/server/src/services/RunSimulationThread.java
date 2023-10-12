package services;

import simulation.Simulation;

public class RunSimulationThread implements Runnable {
    private Simulation simulation;

    public RunSimulationThread(Simulation simulation) {
        this.simulation = simulation;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    @Override
    public void run() {
        while (!simulation.isStopped()) {
            simulation.runTick();
        }
        System.out.println("Ended simulation " + simulation.getId());
    }
}
