package components.pages.results.runtime;

import engine.SimulationDto;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.SimpleItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RuntimeController implements Initializable {

    @FXML
    private ProgressBar progressBar;
    private Pane placeholder;
    private VBox simulationPlaceholder;

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

    private SimulationDto simulationDto;

    public RuntimeController() {
        runTimeProp = new SimpleLongProperty();
        ticksProp = new SimpleIntegerProperty();
        tableItems = new ArrayList<>();
        progressProp = new SimpleDoubleProperty();
    }

    public void setSimulationDto(SimulationDto simulationDto) {
        this.simulationDto = simulationDto;
        setDisplay();
    }

    public void setSimulationPlaceholder(VBox simulationPlaceholder) {
        this.simulationPlaceholder = simulationPlaceholder;
    }

    public void setPlaceholder(Pane placeholder) {
        this.placeholder = placeholder;
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
        runTimeProp.set(simulationDto.getRunTimeSeconds());
        ticksProp.set(simulationDto.getTicks());
        progressProp.set(simulationDto.getProgress());
        stopBtn.setDisable(!simulationDto.getTerminationDto().isStoppedByUser());
        tableItems.clear();
        simulationDto.getPopulations().forEach((entityName, population) -> {
            tableItems.add(new SimpleItem(entityName, String.valueOf(population)));
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
//        progressProp.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
//            if(newValue.equals(1.0)) {
//                loadStatistics(currentSimulation);
//            }
//        }));
    }

    @FXML
    void rerunSimulation(ActionEvent event) {

    }

    @FXML
    void pauseCurrentTask(ActionEvent event) {

    }

    @FXML
    void playCurrentTask(ActionEvent event) {
    }

    @FXML
    void stopCurrentTask(ActionEvent event) {
    }


    @FXML
    void onRunTick(ActionEvent event) {

    }

}
