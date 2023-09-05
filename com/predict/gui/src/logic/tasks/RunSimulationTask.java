package logic.tasks;

import ins.environment.EnvironmentInstance;
import javafx.application.Platform;
import javafx.concurrent.Task;
import simulation.Manager;
import simulation.Simulation;

import java.util.UUID;
import java.util.function.Consumer;

public class RunSimulationTask extends Task<Boolean> {
    private Simulation s;
    private boolean isDisplayed;
    private Consumer<Long> runtimeConsumer;
    private Consumer<Integer> ticksConsumer;
    private Consumer<Long> aliveEntitiesConsumer;
    private Consumer<Double> progressConsumer;


    public RunSimulationTask(Simulation s, Consumer<Long> runtimeConsumer, Consumer<Integer> ticksConsumer, Consumer<Long> aliveEntitiesConsumer, Consumer<Double> progressConsumer) {
        this.s = s;
        this.isDisplayed = false;
        this.runtimeConsumer = runtimeConsumer;
        this.ticksConsumer = ticksConsumer;
        this.aliveEntitiesConsumer = aliveEntitiesConsumer;
        this.progressConsumer = progressConsumer;
    }

    public void updateConsumers(Consumer<Long> runtimeConsumer, Consumer<Integer> ticksConsumer, Consumer<Long> aliveEntitiesConsumer, Consumer<Double> progressConsumer) {
        this.runtimeConsumer = runtimeConsumer;
        this.ticksConsumer = ticksConsumer;
        this.aliveEntitiesConsumer = aliveEntitiesConsumer;
        this.progressConsumer = progressConsumer;
    }

    public EnvironmentInstance getEnv() {
        return s.getEnvironmentInstance();
    }

    public void display() {
        this.isDisplayed = true;
    }

    public void hide() {
        this.isDisplayed = false;
    }

    public String getSimulationId() {
        return s.getId().toString();
    }

    public void pause() {
        s.pause();
    }

    public void play() {
        s.resume();
    }

    public void displayOnConsumers() {
        Platform.runLater(() -> {
            runtimeConsumer.accept(s.getRunTime());
            ticksConsumer.accept(s.getTicks());
            aliveEntitiesConsumer.accept(s.getAliveEntities());
            progressConsumer.accept(s.getProgress());
        });
    }

    @Override
    protected Boolean call() {

        try {
            updateMessage("Start simulation");
            int loops = 0;
            int setEvery = 10000;

            while (!isCancelled()) {
                s.runTick();

                if(isDisplayed && loops%setEvery == 0 && s.isRun()) {
                    displayOnConsumers();
                }

                loops++;
                if(s.isStopped()) cancel();
            }
            if(isCancelled()) {
                displayOnConsumers();
                progressConsumer.accept(1.0);
                s.stop();
            }

            updateMessage("End simulation");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
