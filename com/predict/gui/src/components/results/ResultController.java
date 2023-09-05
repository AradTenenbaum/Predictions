package components.results;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import logic.TasksManager;
import logic.tasks.RunSimulationTask;
import simulation.Manager;

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

    @FXML
    private ListView<String> entityCountList;

    private Manager manager;

    private TasksManager tasksManager;

    private Task<Boolean> currentDisplayed;

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

    public ResultController() {
        runTimeProp = new SimpleLongProperty();
        ticksProp = new SimpleIntegerProperty();
        aliveEntitiesProp = new SimpleLongProperty();
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setTasksManager(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
        this.tasksManager.setRuntimeConsumer(runTimeProp::set);
        this.tasksManager.setTicksConsumer(ticksProp::set);
        this.tasksManager.setAliveEntitiesConsumer(aliveEntitiesProp::set);
        setDisplay();
    }

    public void setDisplay() {
        execList.getItems().clear();
        List<RunSimulationTask> tasks = tasksManager.getTasks();
        for(int i = 0; i < tasks.size(); i++) {
            execList.getItems().add("Simulation " + i);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // bind
        runtimeVal.textProperty().bind(Bindings.format("%d sec", runTimeProp));
        ticksVal.textProperty().bind(Bindings.format("%d", ticksProp));
        aliveEntitiesVal.textProperty().bind(Bindings.format("%d", aliveEntitiesProp));

        execList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedItem = execList.getSelectionModel().getSelectedItem();

                String selected[] = selectedItem.split(" ");
                int index = Integer.parseInt(selected[1]);
                for(int i = 0; i < tasksManager.getTasks().size(); i++) {
                    if(i == index) {
                        currentDisplayed = tasksManager.getTasks().get(index);
                        tasksManager.getTasks().get(i).display();
                    }
                    else {
                        tasksManager.getTasks().get(i).hide();
                    }
                }
            }
        });
    }


    @FXML
    void stopCurrentTask(ActionEvent event) {
        currentDisplayed.cancel();
    }
}
