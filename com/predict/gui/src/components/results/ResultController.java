package components.results;

import components.execution.ExecutionController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.TasksManager;
import logic.tasks.RunSimulationTask;
import simulation.Manager;
import utils.Helpers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    @FXML
    private ListView<String> execList;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button rerunBtn;
    private Pane placeholder;
    @FXML
    private ListView<String> entityCountList;

    private Manager manager;

    private TasksManager tasksManager;

    private RunSimulationTask currentDisplayed;

    @FXML
    private Button stopBtn;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button playBtn;

    @FXML
    private Label runtimeVal;

    @FXML
    private Label ticksVal;

    @FXML
    private Label aliveEntitiesVal;

    private SimpleLongProperty runTimeProp;
    private SimpleIntegerProperty ticksProp;
    private SimpleLongProperty aliveEntitiesProp;
    private SimpleDoubleProperty progressProp;
    private Helpers helpers;

    public ResultController() {
        helpers = new Helpers();
        runTimeProp = new SimpleLongProperty();
        ticksProp = new SimpleIntegerProperty();
        aliveEntitiesProp = new SimpleLongProperty();
        progressProp = new SimpleDoubleProperty();
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setTasksManager(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
        setConsumers();
        setDisplay();
    }

    public void setPlaceholder(Pane placeholder) {
        this.placeholder = placeholder;
    }

    public void setConsumers() {
        this.tasksManager.setConsumers(runTimeProp::set, ticksProp::set, aliveEntitiesProp::set, progressProp::set);
    }

    public void setDisplay() {
        execList.getItems().clear();
        List<RunSimulationTask> tasks = tasksManager.getTasks();
        for(int i = 0; i < tasks.size(); i++) {
            execList.getItems().add(tasks.get(i).getSimulationId());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // bind
        runtimeVal.textProperty().bind(Bindings.format("%d sec", runTimeProp));
        ticksVal.textProperty().bind(Bindings.format("%d", ticksProp));
        aliveEntitiesVal.textProperty().bind(Bindings.format("%d", aliveEntitiesProp));
        progressBar.progressProperty().bind(progressProp);

        execList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedItem = execList.getSelectionModel().getSelectedItem();

                for(int i = 0; i < tasksManager.getTasks().size(); i++) {
                    if(tasksManager.getTasks().get(i).getSimulationId().equals(selectedItem)) {
                        currentDisplayed = tasksManager.getTasks().get(i);
                        currentDisplayed.displayOnConsumers();
                        tasksManager.getTasks().get(i).display();
                    }
                    else {
                        tasksManager.getTasks().get(i).hide();
                    }
                }
            }
        });
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
        manager.setEnvironmentInstance(currentDisplayed.getEnv());
        loadExecution();
    }

    @FXML
    void pauseCurrentTask(ActionEvent event) {
        currentDisplayed.pause();
    }

    @FXML
    void playCurrentTask(ActionEvent event) {
        currentDisplayed.play();
    }

    @FXML
    void stopCurrentTask(ActionEvent event) {
        currentDisplayed.cancel();
    }
}
