package logic;

import javafx.concurrent.Task;
import logic.tasks.RunSimulationTask;
import simulation.Manager;
import utils.SimpleItem;
import utils.exception.SimulationException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TasksManager {
    private List<RunSimulationTask> tasks;
    private ExecutorService executorService;
    private Manager manager;
    private Consumer<Long> runtimeConsumer;
    private Consumer<Integer> ticksConsumer;
    private List<SimpleItem> tableItems;
    private Consumer<Double> progressConsumer;

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

    public void setTableItems(List<SimpleItem> tableItems) {
        this.tableItems = tableItems;
    }

    public void setProgressConsumer(Consumer<Double> progressConsumer) {
        this.progressConsumer = progressConsumer;
    }

    public void setConsumers(Consumer<Long> runtimeConsumer, Consumer<Integer> ticksConsumer, Consumer<Double> progressConsumer, List<SimpleItem> tableItems) {
        setRuntimeConsumer(runtimeConsumer);
        setTicksConsumer(ticksConsumer);
        setProgressConsumer(progressConsumer);
        setTableItems(tableItems);
        tasks.forEach(runSimulationTask -> runSimulationTask.updateConsumers(runtimeConsumer, ticksConsumer, progressConsumer, tableItems));
    }

    public Optional<RunSimulationTask> getTaskBySimulationId(String simulationId) {
        return tasks.stream().filter(runSimulationTask -> runSimulationTask.getSimulationId().equals(simulationId)).findFirst();
    }

    public void runAll() {
        tasks.forEach(booleanTask -> {
            executorService.submit(booleanTask);
        });
    }

    public void runOne() throws SimulationException {
        RunSimulationTask newTask = new RunSimulationTask(manager.generateSimulation(), runtimeConsumer, ticksConsumer, progressConsumer, tableItems);
        this.tasks.add(newTask);
        executorService.submit(newTask);
    }

    public List<RunSimulationTask> getTasks() {
        return tasks;
    }

    public void clear() {
        tasks.forEach(runSimulationTask -> runSimulationTask.cancel());
        executorService.shutdown();
        tasks.clear();
    }

    public void hideAll() {
        tasks.forEach(RunSimulationTask::hide);
    }
}
