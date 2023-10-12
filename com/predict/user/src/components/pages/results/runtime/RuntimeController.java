package components.pages.results.runtime;

import engine.SimulationDto;
import errors.ErrorDialog;
import generic.MessageObject;
import http.HttpClientUtil;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.SimpleItem;
import utils.Url;

import java.io.IOException;
import java.net.HttpURLConnection;
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
    @FXML
    private ListView<String> envListView;

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
        envListView.getItems().clear();
        simulationDto.getEnvValues().forEach((s, s2) -> {
            envListView.getItems().add(s + ": " + s2);
        });
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
    }

    @FXML
    void pauseCurrentTask(ActionEvent event) {
      String fixedUrl = Url.addFirstQueryParam(Constants.SIMULATION_PAUSE_URL, "id", simulationDto.getId().toString());
        HttpClientUtil.runAsyncGet(fixedUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorDialog.send(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != HttpURLConnection.HTTP_OK) {
                    MessageObject messageObject = (MessageObject)HttpClientUtil.fromJsonToObject(response.body(), new MessageObject(""));
                    Platform.runLater(() -> ErrorDialog.send(messageObject.getMessage()));
                } else {
                    System.out.println("Simulation paused");
                }
                response.body().close();
            }
        });
    }

    @FXML
    void playCurrentTask(ActionEvent event) {
        String fixedUrl = Url.addFirstQueryParam(Constants.SIMULATION_PLAY_URL, "id", simulationDto.getId().toString());
        HttpClientUtil.runAsyncGet(fixedUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorDialog.send(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != HttpURLConnection.HTTP_OK) {
                    MessageObject messageObject = (MessageObject)HttpClientUtil.fromJsonToObject(response.body(), new MessageObject(""));
                    Platform.runLater(() -> ErrorDialog.send(messageObject.getMessage()));
                } else {
                    System.out.println("Simulation play");
                }
                response.body().close();
            }
        });
    }

    @FXML
    void stopCurrentTask(ActionEvent event) {
        String fixedUrl = Url.addFirstQueryParam(Constants.SIMULATION_STOP_URL, "id", simulationDto.getId().toString());
        HttpClientUtil.runAsyncGet(fixedUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorDialog.send(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != HttpURLConnection.HTTP_OK) {
                    MessageObject messageObject = (MessageObject)HttpClientUtil.fromJsonToObject(response.body(), new MessageObject(""));
                    Platform.runLater(() -> ErrorDialog.send(messageObject.getMessage()));
                } else {
                    System.out.println("Simulation stopped");
                }
                response.body().close();
            }
        });
    }

}
