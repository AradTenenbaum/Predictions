package logic.tasks;

import javafx.application.Platform;
import javafx.concurrent.Task;
import simulation.Manager;
import simulation.Simulation;

import java.util.function.Consumer;

public class RunSimulationTask extends Task<Boolean> {
    private Simulation s;
    private boolean isDisplayed;
    private Consumer<Long> runtimeConsumer;
    private Consumer<Integer> ticksConsumer;
    private Consumer<Long> aliveEntitiesConsumer;

    public RunSimulationTask(Simulation s, Consumer<Long> runtimeConsumer, Consumer<Integer> ticksConsumer, Consumer<Long> aliveEntitiesConsumer) {
        this.s = s;
        this.isDisplayed = false;
        this.runtimeConsumer = runtimeConsumer;
        this.ticksConsumer = ticksConsumer;
        this.aliveEntitiesConsumer = aliveEntitiesConsumer;
    }

    public void display() {
        this.isDisplayed = true;
    }

    public void hide() {
        this.isDisplayed = false;
    }

    @Override
    protected Boolean call() {

        try {
            updateMessage("Start simulation");
            int loops = 0;
            int setEvery = 2000;

            while (!isCancelled()) {
                s.runTick();
                long runtime = s.getRunTime();
                int ticks = s.getTicks();
                long aliveEntities = s.getAliveEntities();
                System.out.println(runtime + " " + ticks + " " + aliveEntities);

                if(isDisplayed && loops%setEvery == 0) {
                    Platform.runLater(() -> {
                        runtimeConsumer.accept(runtime);
                        ticksConsumer.accept(ticks);
                        aliveEntitiesConsumer.accept(aliveEntities);
                    });
                }

                loops++;
            }

            updateMessage("End simulation");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
