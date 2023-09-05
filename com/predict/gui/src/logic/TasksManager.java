package logic;

import javafx.concurrent.Task;
import logic.tasks.RunSimulationTask;
import simulation.Manager;
import utils.exception.SimulationException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TasksManager {
    private List<RunSimulationTask> tasks;
    private ExecutorService executorService;
    private Manager manager;
    private Consumer<Long> runtimeConsumer;
    private Consumer<Integer> ticksConsumer;
    private Consumer<Long> aliveEntitiesConsumer;

    public TasksManager(Manager manager) {
        executorService = Executors.newFixedThreadPool(manager.getThreadsNumber());
        this.tasks = new ArrayList<>();
        this.manager = manager;
    }

    public void setRuntimeConsumer(Consumer<Long> runtimeConsumer) {
        this.runtimeConsumer = runtimeConsumer;
    }

    public void setTicksConsumer(Consumer<Integer> ticksConsumer) {
        this.ticksConsumer = ticksConsumer;
    }

    public void setAliveEntitiesConsumer(Consumer<Long> aliveEntitiesConsumer) {
        this.aliveEntitiesConsumer = aliveEntitiesConsumer;
    }

    public void runAll() {
        tasks.forEach(booleanTask -> {
            executorService.submit(booleanTask);
        });
    }

    public void runOne() throws SimulationException {
        RunSimulationTask newTask = new RunSimulationTask(manager.generateSimulation(), runtimeConsumer, ticksConsumer, aliveEntitiesConsumer);
        this.tasks.add(newTask);
        executorService.submit(newTask);
    }

    public List<RunSimulationTask> getTasks() {
        return tasks;
    }
}
