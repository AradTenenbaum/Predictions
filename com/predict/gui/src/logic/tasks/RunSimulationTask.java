package logic.tasks;

import ins.EntityInstance;
import ins.environment.EnvironmentInstance;
import javafx.application.Platform;
import javafx.concurrent.Task;
import simulation.Manager;
import simulation.Simulation;
import utils.Helpers;
import utils.SimpleItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class RunSimulationTask extends Task<Boolean> {
    private Simulation s;
    private boolean isDisplayed;
    private Consumer<Long> runtimeConsumer;
    private Consumer<Integer> ticksConsumer;
    private Consumer<Double> progressConsumer;
    private List<SimpleItem> tableItems;
    private Helpers helpers;


    public RunSimulationTask(Simulation s, Consumer<Long> runtimeConsumer, Consumer<Integer> ticksConsumer, Consumer<Double> progressConsumer, List<SimpleItem> tableItems) {
        this.s = s;
        this.isDisplayed = false;
        this.runtimeConsumer = runtimeConsumer;
        this.ticksConsumer = ticksConsumer;
        this.progressConsumer = progressConsumer;
        this.tableItems = tableItems;
        this.helpers = new Helpers();
    }

    public void updateConsumers(Consumer<Long> runtimeConsumer, Consumer<Integer> ticksConsumer, Consumer<Double> progressConsumer, List<SimpleItem> tableItems) {
        this.runtimeConsumer = runtimeConsumer;
        this.ticksConsumer = ticksConsumer;
        this.progressConsumer = progressConsumer;
        this.tableItems = tableItems;
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
            if(isDisplayed) {
                runtimeConsumer.accept(s.getRunTime());
                ticksConsumer.accept(s.getTicks());
                progressConsumer.accept(s.getProgress());
                tableItems.forEach(simpleItem -> {
                    simpleItem.setSecond(
                            String.valueOf(s.getEntities().get(simpleItem.getFirst().get()).stream().filter(EntityInstance::getAlive).count())
                    );
                });
            }
        });
    }


    @Override
    protected Boolean call() {

        try {
            updateMessage("Start simulation");
            int loops = 0;
            int setEvery = 1000;

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
            helpers.openErrorDialog("Runtime error occurred when running the simulation");
        }

        return true;
    }
}
