package components.runtime;

import components.execution.ExecutionController;
import components.statistics.StatisticsController;
import ins.EntityInstance;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.TasksManager;
import logic.tasks.RunSimulationTask;
import simulation.Manager;
import simulation.Simulation;
import utils.Helpers;
import utils.SimpleItem;
import utils.exception.SimulationException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RuntimeController implements Initializable {

    @FXML
    private ProgressBar progressBar;
    private Pane placeholder;
    private VBox simulationPlaceholder;
    private Manager manager;

    private TasksManager tasksManager;

    private Simulation currentSimulation;

    @FXML
    private Label runtimeVal;

    @FXML
    private Label ticksVal;
    @FXML
    private Button stopBtn;

    @FXML
    private TableView<SimpleItem> entitiesAmountsTable;

    @FXML
    private TableColumn<SimpleItem, String> entityCol;

    @FXML
    private TableColumn<SimpleItem, String> aliveCol;

    private SimpleLongProperty runTimeProp;
    private SimpleIntegerProperty ticksProp;
    private SimpleDoubleProperty progressProp;
    private List<SimpleItem> tableItems;
    private Helpers helpers;
    private RunSimulationTask task;

    public RuntimeController() {
        helpers = new Helpers();
        runTimeProp = new SimpleLongProperty();
        ticksProp = new SimpleIntegerProperty();
        tableItems = new ArrayList<>();
        progressProp = new SimpleDoubleProperty();
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setTasksManager(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
        setConsumers();
    }

    public void setCurrentSimulation(Simulation currentSimulation) {
        this.currentSimulation = currentSimulation;
        setDisplay();
    }

    public void setTask(RunSimulationTask task) {
        this.task = task;
        task.display();
    }

    public void setSimulationPlaceholder(VBox simulationPlaceholder) {
        this.simulationPlaceholder = simulationPlaceholder;
    }

    public void setPlaceholder(Pane placeholder) {
        this.placeholder = placeholder;
    }

    public void setConsumers() {
        this.tasksManager.setConsumers(runTimeProp::set, ticksProp::set, progressProp::set, tableItems);
    }

    public void setDisplay() {
        entityCol.setCellValueFactory(
                param -> param.getValue().getFirst()
        );
        aliveCol.setCellValueFactory(
                param -> param.getValue().getSecond()
        );

        final ObservableList<SimpleItem> data =
                FXCollections.observableArrayList(
                        tableItems
                );

        entitiesAmountsTable.setItems(data);
        displayOnConsumers();
    }

    public void displayOnConsumers() {
        Simulation s = currentSimulation;
        runTimeProp.set(s.getRunTime());
        ticksProp.set(s.getTicks());
        progressProp.set(s.getProgress());
        stopBtn.setDisable(!s.isUserStop());
        tableItems.clear();
        s.getEntities().forEach((s1, entityInstances) -> {
            tableItems.add(new SimpleItem(s1, String.valueOf(entityInstances.stream().filter(EntityInstance::getAlive).count())));
        });
        final ObservableList<SimpleItem> data =
                FXCollections.observableArrayList(
                        tableItems
                );
        entitiesAmountsTable.setItems(data);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // bind
        runtimeVal.textProperty().bind(Bindings.format("%d sec", runTimeProp));
        ticksVal.textProperty().bind(Bindings.format("%d", ticksProp));
        progressBar.progressProperty().bind(progressProp);
        progressProp.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
            if(newValue.equals(1.0)) {
                loadStatistics(currentSimulation);
            }
        }));
    }

    public void loadStatistics(Simulation simulation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Helpers.STATISTICS_PATH));
            BorderPane component = loader.load();
            StatisticsController statisticsController = loader.getController();
            statisticsController.setManager(manager);
            statisticsController.setTasksManager(tasksManager);
            statisticsController.setPlaceholder(placeholder);
            statisticsController.setCurrentSimulation(simulation);
            helpers.fitParent(simulationPlaceholder, component);

            simulationPlaceholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadExecution() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Helpers.EXECUTION_PATH));
            Pane component = loader.load();
            ExecutionController executionController = loader.getController();
            executionController.setManager(manager);
            executionController.setTasksManager(tasksManager);
            executionController.setPlaceholder(placeholder);
            helpers.fitParent(placeholder, component);

            VBox p = (VBox) component.getChildren().get(0);
            p.prefWidthProperty().bind(component.widthProperty());
            p.prefHeightProperty().bind(component.heightProperty());

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void rerunSimulation(ActionEvent event) {
        try {
            manager.setEnvironmentInstance(currentSimulation.getEnvironmentInstance(), currentSimulation.getPopulations(), currentSimulation.getWorldVersion());
            manager.setPopulations(currentSimulation.getPopulations());
            loadExecution();
        } catch (SimulationException e) {
            helpers.openErrorDialog(e.getMessage());
        }
    }

    @FXML
    void pauseCurrentTask(ActionEvent event) {
        currentSimulation.pause();
        displayOnConsumers();
    }

    @FXML
    void playCurrentTask(ActionEvent event) {
        currentSimulation.resume();
    }

    @FXML
    void stopCurrentTask(ActionEvent event) {
        task.cancel();
    }


    @FXML
    void onRunTick(ActionEvent event) {
        if(currentSimulation.isPaused()) {
            currentSimulation.runOneTick();
            displayOnConsumers();
        }
    }

}
