package components.results;

import components.execution.ExecutionController;
import components.runtime.RuntimeController;
import components.statistics.StatisticsController;
import data.dto.PropertyDto;
import ins.EntityInstance;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.TasksManager;
import logic.tasks.RunSimulationTask;
import simulation.Manager;
import simulation.Simulation;
import utils.Helpers;
import utils.SimpleItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    @FXML
    private ListView<String> execList;
    private Pane placeholder;
    private Manager manager;
    private TasksManager tasksManager;

    @FXML
    private VBox simulationPlaceholder;
    private Helpers helpers;

    public ResultController() {
        helpers = new Helpers();
    }

    public void setManager(Manager manager) {
        this.manager = manager;
        setDisplay();
    }

    public void setTasksManager(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    public void setPlaceholder(Pane placeholder) {
        this.placeholder = placeholder;
    }

    public void setDisplay() {
        execList.getItems().clear();
        List<Simulation> simulations = manager.getSimulations();
        for(int i = 0; i < simulations.size(); i++) {
            execList.getItems().add(simulations.get(i).getId().toString());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        execList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String simulationId = execList.getSelectionModel().getSelectedItem();

                tasksManager.hideAll();
                Optional<Simulation> sim = manager.getSimulations().stream().filter(simulation -> simulation.getId().toString().equals(simulationId)).findFirst();
                sim.ifPresent(simulation -> {
                    if(simulation.isStopped()) {
                        loadStatistics(simulation);
                    } else {
                        loadRuntime(simulation);
                    }
                });

            }
        });
    }

    public void loadStatistics(Simulation simulation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Helpers.STATISTICS_PATH));
            BorderPane component = loader.load();
            StatisticsController statisticsController = loader.getController();
            statisticsController.setCurrentSimulation(simulation);
            helpers.fitParent(simulationPlaceholder, component);

            simulationPlaceholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRuntime(Simulation simulation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Helpers.RUNTIME_PATH));
            BorderPane component = loader.load();
            RuntimeController runtimeController = loader.getController();
            runtimeController.setManager(manager);
            runtimeController.setTasksManager(tasksManager);
            runtimeController.setPlaceholder(placeholder);
            runtimeController.setCurrentSimulation(simulation);
            Optional<RunSimulationTask> task = tasksManager.getTaskBySimulationId(simulation.getId().toString());
            task.ifPresent(runtimeController::setTask);

            helpers.fitParent(simulationPlaceholder, component);

            simulationPlaceholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
